package nl.infosupport.agenttalks.podcast.service;

import java.time.LocalDate;

import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.podcast.clients.content.ContentClient;

public class PodcastGenerationProcess {
    @Inject
    ContentClient contentClient;

    @Inject
    PodcastOrchestrator podcastOrchestrator;

    Logger logger = Logger.getLogger(PodcastGenerationProcess.class);

    @Scheduled(cron = "0 0 18 ? * FRI") // Every Friday at 18:00
    public void start() {
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
                podcastOrchestrator.generatePodcast(startDate, currentDate, processableSubmissions);
                logger.info("Podcast generation completed successfully");
            } catch (Exception e) {
                logger.errorf(e, "Podcast generation failed for period %s to %s", startDate, currentDate);
            }
        }
    }
}
