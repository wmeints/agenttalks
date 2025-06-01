package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

/**
 * Mixes the intro, content, and outro audio files for a podcast episode.
 */
public class PodcastAudioMixer {
    @Incoming("audio-mixing-input")
    @Outgoing("audio-mixing-output")
    public PodcastEpisodeData process(PodcastEpisodeData message) {
        return message;
    }
}
