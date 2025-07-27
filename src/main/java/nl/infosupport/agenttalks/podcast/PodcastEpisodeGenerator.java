package nl.infosupport.agenttalks.podcast;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.eventbus.EventBus;
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

    @Inject
    EventBus eventBus;

    @Blocking
    @Scheduled(cron = "0 0 18 ? * FRI")
    public Uni<Void> generatePodcastEpisode() {
        return Uni.createFrom().item(() -> {
            internalGeneratePodcastEpisode();
            return (Void) null;
        }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    void internalGeneratePodcastEpisode() {
        var pendingSubmissions = ContentSubmission.findPending().list();

        logger.infof("Processing %d pending submissions", pendingSubmissions.size());

        PodcastScript episodeScript = generatePodcastScript(pendingSubmissions);
        String audioFilePath = podcastAudioGenerator.generatePodcastAudio(episodeScript);

        // logger.info("Collecting data for show notes and episode description");

        // String formattedSubmissions = formatContentSubmissions(pendingSubmissions);
        // String formattedPodcastScript = formatPodcastScript(episodeScript);

        // logger.info("Generating show notes");

        // String showNotes = showNotesGenerator.generateShowNotes(
        // formattedPodcastScript, formattedSubmissions);

        // logger.info("Generating episode description");

        // String episodeDescription =
        // episodeSummaryGenerator.generateEpisodeDescription(
        // formattedPodcastScript,
        // formattedSubmissions);

        PodcastEpisode episode = new PodcastEpisode(
                episodeScript,
                audioFilePath,
                PodcastEpisode.getNextEpisodeNumber(),
                "NOT GENERATED YET",
                "NOT GENERATED YET");

        savePodcastEpisode(episode);
        markContentSubmissionsAsProcessed(pendingSubmissions);

        buzzsproutClient.createEpisode(podcastId,
                new CreateEpisodeRequest(
                        episode.title,
                        episode.description,
                        episode.summary,
                        episode.audioFilePath));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void savePodcastEpisode(PodcastEpisode episode) {
        episode.persistAndFlush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void markContentSubmissionsAsProcessed(List<ContentSubmission> submissions) {
        for (ContentSubmission submission : submissions) {
            submission.markAsProcessed();
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
        logger.info("Generating podcast script");

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
}
