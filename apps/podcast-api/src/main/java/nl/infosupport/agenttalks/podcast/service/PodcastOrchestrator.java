package nl.infosupport.agenttalks.podcast.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.azure.storage.blob.BlobServiceClient;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.BuzzsproutClient;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.model.CreateEpisodeRequest;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.model.CreateEpisodeResponse;
import nl.infosupport.agenttalks.podcast.clients.content.ContentClient;
import nl.infosupport.agenttalks.podcast.clients.content.model.ContentSubmission;
import nl.infosupport.agenttalks.podcast.clients.content.model.CreatePodcastEpisode;
import nl.infosupport.agenttalks.podcast.clients.content.model.MarkAsProcessed;
import nl.infosupport.agenttalks.podcast.clients.content.model.MarkForProcessing;
import nl.infosupport.agenttalks.podcast.clients.content.model.PodcastEpisode;
import nl.infosupport.agenttalks.podcast.model.PodcastHost;
import nl.infosupport.agenttalks.podcast.model.PodcastScript;

@ApplicationScoped
public class PodcastOrchestrator {

    @Inject
    ContentClient contentClient;

    @Inject
    BlobServiceClient blobServiceClient;

    @Inject
    PodcastScriptGenerator podcastScriptGenerator;

    @Inject
    AudioProcessor audioProcessor;

    @Inject
    @RestClient
    BuzzsproutClient buzzsproutClient;

    @ConfigProperty(name = "buzzsprout.podcast-id")
    String podcastId;

    private static final Logger logger = Logger.getLogger(PodcastOrchestrator.class);

    @Scheduled(cron = "0 0 18 ? * FRI") // Every Friday at 18:00
    public void runWeeklyPodcastGeneration() {
        var currentDate = LocalDate.now();
        var startDate = currentDate.minusDays(7); // One week ago

        var processableSubmissions = contentClient.findProcessableSubmissions(startDate, currentDate);

        logger.infof(
                "Found %d processable submissions for the week starting %s",
                processableSubmissions.size(), startDate);

        if (!processableSubmissions.isEmpty()) {
            logger.infof(
                    "Triggering podcast generation for submissions from %s to %s",
                    startDate, currentDate);

            try {
                generatePodcast(startDate, currentDate, processableSubmissions);
                logger.info("Weekly podcast generation completed successfully");
            } catch (Exception e) {
                logger.errorf(e, "Weekly podcast generation failed for period %s to %s", startDate, currentDate);
            }
        } else {
            logger.info("No processable submissions found for this week");
        }
    }

    public void generatePodcast(LocalDate startDate, LocalDate endDate, List<ContentSubmission> contentSubmissions) {
        logger.infof("Starting podcast generation for %d submissions from %s to %s",
                contentSubmissions.size(), startDate, endDate);

        try {
            // Step 1: Lock content submissions
            lockContentSubmissions(contentSubmissions);
            logger.info("Content submissions locked successfully");

            // Step 2: Generate podcast script
            PodcastScript script = generatePodcastScriptInternal(contentSubmissions);
            logger.infof("Generated podcast script with title: %s", script.title);

            // Step 3: Generate audio for each script fragment
            var processableFragments = script.sections.stream()
                    .flatMap(section -> section.fragments.stream())
                    .collect(Collectors.toList());

            var generatedFragments = processableFragments.stream()
                    .map(fragment -> audioProcessor.generateSpeech(fragment))
                    .collect(Collectors.toList());
            logger.infof("Generated %d audio fragments", generatedFragments.size());

            // Step 4: Concatenate audio fragments
            var contentAudioFile = audioProcessor.concatenateAudioFragments(generatedFragments);
            logger.infof("Concatenated audio fragments into: %s", contentAudioFile);

            // Step 5: Save episode metadata
            // Note: Intro/outro are handled by Buzzsprout during publishing
            PodcastEpisode episodeMetadata = savePodcastEpisode(script.title, contentAudioFile, contentSubmissions);
            logger.infof("Saved episode metadata with episode number: %d", episodeMetadata.episodeNumber);

            // Step 6: Publish to Buzzsprout
            publishPodcastEpisode(
                    1, episodeMetadata.episodeNumber,
                    script.title,
                    episodeMetadata.description,
                    episodeMetadata.showNotes,
                    episodeMetadata.audioFilePath);
            logger.info("Published episode to Buzzsprout successfully");

            // Step 7: Mark submissions as processed
            markContentSubmissionsAsProcessed(contentSubmissions);
            logger.info("Marked content submissions as processed");

            logger.info("Podcast generation completed successfully");

        } catch (Exception e) {
            logger.errorf(e, "Podcast generation failed");
            throw new PodcastGenerationException("Failed to generate podcast", e);
        }
    }

    private void lockContentSubmissions(List<ContentSubmission> contentSubmissions) {
        logger.infof("Locking %d content submissions", contentSubmissions.size());

        for (var contentSubmission : contentSubmissions) {
            contentClient.markForProcessing(new MarkForProcessing(contentSubmission.id));
        }
    }

    private void markContentSubmissionsAsProcessed(List<ContentSubmission> contentSubmissions) {
        logger.infof("Marking %d content submissions as processed", contentSubmissions.size());

        for (var contentSubmission : contentSubmissions) {
            contentClient.markAsProcessed(new MarkAsProcessed(contentSubmission.id));
        }
    }

    private PodcastScript generatePodcastScriptInternal(List<ContentSubmission> contentSubmissions) {
        logger.infof("Generating podcast script for %d content submissions", contentSubmissions.size());

        var firstPodcastHost = PodcastHost.findByIndex(1);
        var secondPodcastHost = PodcastHost.findByIndex(2);

        if (firstPodcastHost == null || secondPodcastHost == null) {
            throw new RuntimeException("Podcast hosts not found in database");
        }

        var combinedContent = combineContentSubmissions(contentSubmissions);

        var script = podcastScriptGenerator.generatePodcastScript(
                firstPodcastHost.name, secondPodcastHost.name,
                firstPodcastHost.styleInstructions, secondPodcastHost.styleInstructions,
                combinedContent);

        logger.infof("Successfully generated script with title: %s", script.title);
        return script;
    }

    private String combineContentSubmissions(List<ContentSubmission> contentSubmissions) {
        StringBuilder combinedContent = new StringBuilder();

        for (var contentSubmission : contentSubmissions) {
            combinedContent.append(String.format("%s\n\n", contentSubmission.title));
            combinedContent.append(String.format("%s\n\n", contentSubmission.summary));
            combinedContent.append(String.format("URL: %s\n\n", contentSubmission.url));
        }

        return combinedContent.toString();
    }

    @Retry(maxRetries = 3, delay = 2000, delayUnit = java.time.temporal.ChronoUnit.MILLIS)
    PodcastEpisode savePodcastEpisode(String title, String audioFilePath,
            List<ContentSubmission> contentSubmissions) {
        logger.infof("Saving podcast episode: %s with audio file: %s", title, audioFilePath);

        var audioFile = new File(audioFilePath);
        var episodesContainer = blobServiceClient.getBlobContainerClient("episodes");

        episodesContainer.createIfNotExists();

        try (var inputStream = new FileInputStream(audioFile)) {
            var blob = episodesContainer.getBlobClient(audioFile.getName());
            blob.upload(inputStream);

            CreatePodcastEpisode createPodcastEpisodeInput = new CreatePodcastEpisode(
                    blob.getBlobName(), title,
                    buildShowNotes(contentSubmissions), buildDescription(contentSubmissions));

            var podcastEpisode = contentClient.createPodcastEpisode(createPodcastEpisodeInput);

            if (podcastEpisode.hasErrors()) {
                throw new RuntimeException("Failed to create podcast episode");
            }

            logger.infof("Successfully saved podcast episode with title: %s", podcastEpisode.get().title);
            return podcastEpisode.get();
        } catch (FileNotFoundException e) {
            logger.errorf("Audio file not found: %s", audioFilePath);
            throw new RuntimeException(String.format("Audio file not found: %s", audioFilePath), e);
        } catch (IOException e) {
            logger.errorf("Error uploading audio file: %s", audioFilePath);
            throw new RuntimeException(String.format("Error uploading audio file: %s", audioFilePath), e);
        }
    }

    private String buildShowNotes(List<ContentSubmission> contentSubmissions) {
        StringBuilder showNotes = new StringBuilder();
        showNotes.append("In this episode, we cover:\n\n");

        for (ContentSubmission submission : contentSubmissions) {
            if (submission.title != null && !submission.title.isEmpty()) {
                showNotes.append("• ").append(submission.title);
                if (submission.url != null && !submission.url.isEmpty()) {
                    showNotes.append(" (").append(submission.url).append(")");
                }
                showNotes.append("\n");
            }
        }

        return showNotes.toString();
    }

    private String buildDescription(List<ContentSubmission> contentSubmissions) {
        StringBuilder description = new StringBuilder();
        description.append("This episode covers the latest developments in technology and software engineering. ");
        description.append("We discuss ");

        if (!contentSubmissions.isEmpty()) {
            for (int i = 0; i < contentSubmissions.size(); i++) {
                ContentSubmission submission = contentSubmissions.get(i);
                if (submission.title != null && !submission.title.isEmpty()) {
                    if (i > 0 && i == contentSubmissions.size() - 1) {
                        description.append(" and ");
                    } else if (i > 0) {
                        description.append(", ");
                    }
                    description.append(submission.title.toLowerCase());
                }
            }
            description.append(".");
        }

        return description.toString();
    }

    @Retry(maxRetries = 3, delay = 1000, delayUnit = java.time.temporal.ChronoUnit.MILLIS)
    String publishPodcastEpisode(int seasonNumber, int episodeNumber, String title, String description,
            String showNotes, String audioFileUrl) {
        logger.infof("Publishing podcast episode to Buzzsprout: %s (Season %d, Episode %d)",
                title, seasonNumber, episodeNumber);

        try {
            CreateEpisodeRequest request = new CreateEpisodeRequest(
                    seasonNumber, episodeNumber, title, description,
                    showNotes, audioFileUrl);

            CreateEpisodeResponse response = buzzsproutClient.createEpisode(podcastId, request);

            logger.infof("Successfully published episode to Buzzsprout with ID: %d", response.id);

            return response.id.toString();
        } catch (Exception e) {
            logger.errorf(e, "Failed to publish episode to Buzzsprout: %s", title);
            throw new RuntimeException("Failed to publish episode to Buzzsprout", e);
        }
    }

    public static class PodcastGenerationException extends RuntimeException {
        public PodcastGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
