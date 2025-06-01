package nl.fizzylogic.newscast.podcast.processing;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@QuarkusTest
public class PodcastEpisodePublisherTest {
    @Inject
    PodcastEpisodePublisher podcastEpisodePublisher;

    @Test
    public void canPublishPodcastEpisode() {
        var podcastData = new PodcastEpisodeData();
        podcastEpisodePublisher.process(podcastData);
    }
}
