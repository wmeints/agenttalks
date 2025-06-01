package nl.fizzylogic.newscast.podcast.processing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.SubmissionStatus;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@QuarkusTest
public class ContentSubmissionLockerTest {
    @Inject
    ContentSubmissionLocker contentSubmissionLocking;

    @InjectMock
    ContentClient contentClient;

    @Test
    public void canLockContentSubmissions() {
        when(contentClient.markForProcessing(any())).thenReturn(ErrorOr.of(new ContentSubmission()));

        List<ContentSubmission> processableItems = List.of(
                new ContentSubmission(1L, "Test Submission 1", "test", SubmissionStatus.SUMMARIZED),
                new ContentSubmission(2L, "Test Submission 2", "test", SubmissionStatus.SUMMARIZED));

        var trigger = new PodcastEpisodeData(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                processableItems);

        var output = contentSubmissionLocking.process(trigger);

        assertNotNull(output);
        verify(contentClient, times(2)).markForProcessing(any());
    }
}
