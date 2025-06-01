package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkForProcessing;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@ApplicationScoped
public class ContentSubmissionLocking {
    @Inject
    ContentClient contentClient;

    Logger logger = Logger.getLogger(ContentSubmissionLocking.class);

    @Incoming("content-submission-locking-input")
    @Outgoing("content-submission-locking-output")
    public JsonObject process(JsonObject message) {
        var trigger = message.mapTo(PodcastEpisodeData.class);

        logger.infof("Locking %d content submissions for processing", trigger.contentSubmissions.size());

        for (var submission : trigger.contentSubmissions) {
            contentClient.markForProcessing(new MarkForProcessing(submission.id));
        }

        logger.infof("Locked %d content submissions for processing", trigger.contentSubmissions.size());

        return message;
    }
}
