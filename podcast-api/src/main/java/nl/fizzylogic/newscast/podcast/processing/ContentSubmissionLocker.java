package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkForProcessing;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

/**
 * Locks content submissions for processing into a podcast episode.
 */
@ApplicationScoped
public class ContentSubmissionLocker {
    @Inject
    ContentClient contentClient;

    Logger logger = Logger.getLogger(ContentSubmissionLocker.class);

    @Incoming("content-submission-locking-input")
    @Outgoing("content-submission-locking-output")
    public PodcastEpisodeData process(PodcastEpisodeData episodeData) {
        logger.infof("Locking %d content submissions for processing", episodeData.contentSubmissions.size());

        for (var submission : episodeData.contentSubmissions) {
            contentClient.markForProcessing(new MarkForProcessing(submission.id));
        }

        logger.infof("Locked %d content submissions for processing", episodeData.contentSubmissions.size());

        return episodeData;
    }
}
