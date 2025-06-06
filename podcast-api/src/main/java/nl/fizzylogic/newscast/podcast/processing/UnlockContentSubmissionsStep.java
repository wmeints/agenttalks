package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkAsProcessed;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@ApplicationScoped
public class UnlockContentSubmissionsStep {
    @Inject
    ContentClient contentClient;

    Logger logger = Logger.getLogger(UnlockContentSubmissionsStep.class);

    @Incoming("submission-unlocking-input")
    public void process(PodcastEpisodeData episodeData) {
        logger.infof("Unlocking %d content submissions", episodeData.contentSubmissions.size());

        for (var submission : episodeData.contentSubmissions) {
            contentClient.markAsProcessed(new MarkAsProcessed(submission.id));
        }

        logger.infof("Unlocked %d content submissions", episodeData.contentSubmissions.size());
    }
}
