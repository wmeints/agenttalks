package nl.fizzylogic.newscast.podcast.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.SubmissionStatus;

@QuarkusTest
public class PodcastGenerationTest {
    @Inject
    PodcastGeneration podcastGeneration;

    @InjectMock
    ContentClient contentClient;

    Emitter<JsonObject> podcastGenerationTriggerOutput;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setupTestCase() {
        podcastGenerationTriggerOutput = mock(Emitter.class);
        podcastGeneration.podcastGenerationTriggerOutput = podcastGenerationTriggerOutput;
    }

    @Test
    public void canTriggerPodcastGenerationWithContent() {
        List<ContentSubmission> processableItems = List.of(
                new ContentSubmission(1L, "Test Submission 1", "test", SubmissionStatus.SUMMARIZED),
                new ContentSubmission(2L, "Test Submission 2", "test", SubmissionStatus.SUMMARIZED));

        when(contentClient.findProcessableSubmissions(any(), any()))
                .thenReturn(processableItems);

        podcastGeneration.generatePodcastEpisode();

        verify(podcastGenerationTriggerOutput, times(1)).send(any(JsonObject.class));
    }

    @Test
    public void doesntTriggerPodcastGenerationWithoutContent() {
        when(contentClient.findProcessableSubmissions(any(), any()))
                .thenReturn(Collections.emptyList());

        podcastGeneration.generatePodcastEpisode();

        verify(podcastGenerationTriggerOutput, times(0)).send(any(JsonObject.class));
    }
}
