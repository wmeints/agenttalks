package nl.fizzylogic.newscast.podcast.processing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkAsProcessed;
import nl.fizzylogic.newscast.podcast.clients.content.model.SubmissionStatus;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@QuarkusTest
public class ContentSubmissionUnlockerTest {
    @Inject
    UnlockContentSubmissionsStep contentSubmissionUnlocker;

    @InjectMock
    ContentClient contentClient;

    @Test
    public void canUnlockContentAfterProcessing() {
        var processedContentSubmissions = List.of(
                new ContentSubmission(1L, "Content 1", "Test", SubmissionStatus.PROCESSING,
                        "http://localhost:3000/"),
                new ContentSubmission(2L, "Content 2", "Test", SubmissionStatus.PROCESSING,
                        "http://localhost:3001/"),
                new ContentSubmission(3L, "Content 3", "Test", SubmissionStatus.PROCESSING,
                        "http://localhost:3002/"));

        var episodeData = new PodcastEpisodeData(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                processedContentSubmissions);

        contentSubmissionUnlocker.process(episodeData);

        verify(contentClient, times(3)).markAsProcessed(any(MarkAsProcessed.class));
    }
}
