package nl.infosupport.agenttalks.podcast;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.content.ContentSubmission;
import nl.infosupport.agenttalks.podcast.buzzsprout.BuzzsproutClient;
import nl.infosupport.agenttalks.podcast.buzzsprout.CreateEpisodeRequest;

public class PodcastEpisodeGenerator {
    private static final Logger logger = Logger.getLogger(PodcastEpisodeGenerator.class);

    @Inject
    PodcastScriptGenerator podcastScriptGenerator;

    @Inject
    PodcastAudioGenerator podcastAudioGenerator;

    @Inject
    EpisodeSummaryGenerator episodeSummaryGenerator;

    @Inject
    ShowNotesGenerator showNotesGenerator;

    @RestClient
    BuzzsproutClient buzzsproutClient;

    @ConfigProperty(name = "agenttalks.buzzsprout.podcast-id")
    String podcastId;

    @Transactional
    @Scheduled(cron = "0 0 18 ? * FRI")
    public void generatePodcastEpisode() {
        var pendingSubmissions = ContentSubmission.findPending().list();

        logger.infof("Processing %d pending submissions", pendingSubmissions.size());

        lockContentSubmissions(pendingSubmissions);
        PodcastScript episodeScript = generatePodcastScript(pendingSubmissions);
        String audioFilePath = podcastAudioGenerator.generatePodcastAudio(episodeScript);
        String formattedSubmissions = formatContentSubmissions(pendingSubmissions);
        String formattedPodcastScript = formatPodcastScript(episodeScript);
        String showNotes = showNotesGenerator.generateShowNotes(formattedPodcastScript, formattedSubmissions);
        String episodeDescription = episodeSummaryGenerator.generateEpisodeDescription(
                formattedPodcastScript,
                formattedSubmissions);

        PodcastEpisode episode = new PodcastEpisode(
                episodeScript,
                audioFilePath,
                PodcastEpisode.getNextEpisodeNumber(),
                showNotes,
                episodeDescription);

        episode.persistAndFlush();

        buzzsproutClient.createEpisode(podcastId,
                new CreateEpisodeRequest(
                        episode.title,
                        episode.description,
                        episode.summary,
                        episode.audioFilePath));

        unlockContentSubmissions(pendingSubmissions);
    }

    private void unlockContentSubmissions(List<ContentSubmission> pendingSubmissions) {
        for (var submission : pendingSubmissions) {
            submission.markAsProcessed();
            submission.persist();
        }
    }

    private String formatPodcastScript(PodcastScript episodeScript) {
        var outputBuilder = new StringBuilder();

        for (var section : episodeScript.sections) {
            outputBuilder.append(String.format("## %s\n\n", section.title));

            for (var fragment : section.fragments) {
                outputBuilder.append(String.format("%s: %s\n", fragment.host, fragment.content));
            }

            outputBuilder.append("\n");
        }

        return outputBuilder.toString();
    }

    private PodcastScript generatePodcastScript(List<ContentSubmission> pendingSubmissions) {
        var firstHost = PodcastHost.findFirstHost();
        var secondHost = PodcastHost.findSecondHost();

        return podcastScriptGenerator.generatePodcastScript(
                firstHost.name,
                secondHost.name,
                firstHost.styleInstructions,
                secondHost.styleInstructions,
                formatContentSubmissions(pendingSubmissions));
    }

    private String formatContentSubmissions(List<ContentSubmission> pendingSubmissions) {
        var outputBuilder = new StringBuilder();

        for (var submission : pendingSubmissions) {
            outputBuilder.append(String.format("## %s\n\n", submission.title));
            outputBuilder.append(String.format("%s\n\n", submission.summary));
            outputBuilder.append(String.format("Url: %s\n\n", submission.url));
        }

        return outputBuilder.toString();
    }

    private void lockContentSubmissions(List<ContentSubmission> pendingSubmissions) {
        for (var submission : pendingSubmissions) {
            submission.markForProcessing();
            submission.persist();
        }
    }
}
