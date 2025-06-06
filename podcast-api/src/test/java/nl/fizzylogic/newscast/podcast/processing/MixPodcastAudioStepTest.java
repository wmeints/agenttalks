package nl.fizzylogic.newscast.podcast.processing;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@QuarkusTest
public class MixPodcastAudioStepTest {
    @Inject
    MixPodcastAudioStep podcastAudioMixer;

    @Test
    public void canMixPodcastAudio() {
        var podcastData = new PodcastEpisodeData();
        var response = podcastAudioMixer.process(podcastData);

        assertNotNull(response);
    }
}
