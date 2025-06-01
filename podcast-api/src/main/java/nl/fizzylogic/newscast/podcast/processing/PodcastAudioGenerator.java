package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

/**
 * Converts the podcast script into audio using ElevenLabs.
 */
@ApplicationScoped
public class PodcastAudioGenerator {
    @Incoming("audio-generation-input")
    @Outgoing("audio-generation-output")
    public PodcastEpisodeData process(PodcastEpisodeData episodeData) {
        return episodeData;
    }
}
