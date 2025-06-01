package nl.fizzylogic.newscast.podcast.service;

import java.time.LocalDate;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

public class PodcastGeneration {
    @Inject
    ContentClient contentClient;

    @Inject
    @Channel("podcast-generation-trigger-output")
    Emitter<JsonObject> podcastGenerationTriggerOutput;

    Logger logger = Logger.getLogger(PodcastGeneration.class);

    @Scheduled(cron = "0 0 18 ? * FRI") // Every Friday at 18:00
    public void generatePodcastEpisode() {
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

            var generationTrigger = new PodcastEpisodeData(
                    startDate, currentDate, processableSubmissions);

            podcastGenerationTriggerOutput.send(JsonObject.mapFrom(generationTrigger));
        }
    }
}
