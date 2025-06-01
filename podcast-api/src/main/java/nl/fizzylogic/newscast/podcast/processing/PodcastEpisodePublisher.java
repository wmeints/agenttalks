package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

/**
 * Stores the podcast metadata in the content API and publishes the final
 * episode to Buzzsprout.
 */
@ApplicationScoped
public class PodcastEpisodePublisher {
    @Incoming("publication-input")
    @Outgoing("publication-output")
    public PodcastEpisodeData process(PodcastEpisodeData episodeData) {
        return episodeData;
    }
}
