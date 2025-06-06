package nl.fizzylogic.newscast.podcast.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.SubmissionStatus;

@QuarkusTest
public class PodcastGenerationProcessTest {
    @Inject
    PodcastGenerationProcess podcastGeneration;

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
                new ContentSubmission(1L, "Test Submission 1", "test", SubmissionStatus.SUMMARIZED,
                        "http://localhost:3000/"),
                new ContentSubmission(2L, "Test Submission 2", "test", SubmissionStatus.SUMMARIZED,
                        "http://localhost:3001/"));

        when(contentClient.findProcessableSubmissions(any(), any()))
                .thenReturn(processableItems);

        podcastGeneration.start();

        verify(podcastGenerationTriggerOutput, times(1)).send(any(JsonObject.class));
    }

    @Test
    public void doesntTriggerPodcastGenerationWithoutContent() {
        when(contentClient.findProcessableSubmissions(any(), any()))
                .thenReturn(Collections.emptyList());

        podcastGeneration.start();

        verify(podcastGenerationTriggerOutput, times(0)).send(any(JsonObject.class));
    }

    @Test
    public void passesCorrectDateRangeToFindProcessableSubmissions() {
        when(contentClient.findProcessableSubmissions(any(), any()))
                .thenReturn(List.of(new ContentSubmission(1L, "Test", "test", SubmissionStatus.SUMMARIZED, "url")));

        podcastGeneration.start();

        ArgumentCaptor<LocalDate> startCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(contentClient).findProcessableSubmissions(startCaptor.capture(), endCaptor.capture());

        LocalDate expectedEnd = LocalDate.now();
        LocalDate expectedStart = expectedEnd.minusDays(7);
        // Allow for test execution on boundary of midnight
        assert (startCaptor.getValue().equals(expectedStart));
        assert (endCaptor.getValue().equals(expectedEnd));
    }

    @Test
    public void emittedJsonObjectContainsCorrectData() {
        List<ContentSubmission> processableItems = List.of(
                new ContentSubmission(1L, "Test Submission 1", "test", SubmissionStatus.SUMMARIZED,
                        "http://localhost:3000/"));
        when(contentClient.findProcessableSubmissions(any(), any())).thenReturn(processableItems);

        ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
        podcastGeneration.start();
        verify(podcastGenerationTriggerOutput).send(jsonCaptor.capture());
        JsonObject sent = jsonCaptor.getValue();

        assert (sent.containsKey("startDate"));
        assert (sent.containsKey("endDate"));
        assert (sent.containsKey("contentSubmissions"));
        assert (sent.getJsonArray("contentSubmissions").size() == 1);
    }

    @Test
    public void exceptionInFindProcessableSubmissionsDoesNotEmit() {
        doThrow(new RuntimeException("fail")).when(contentClient).findProcessableSubmissions(any(), any());

        try {
            podcastGeneration.start();
        } catch (Exception ignored) {
            // Ignore the exception thrown by the test framework
        }

        verify(podcastGenerationTriggerOutput, never()).send(any(JsonObject.class));
    }

    @Test
    public void nullReturnFromFindProcessableSubmissionsDoesNotEmit() {
        when(contentClient.findProcessableSubmissions(any(), any())).thenReturn(null);

        try {
            podcastGeneration.start();
        } catch (Exception ignored) {
            // Ignore the exception thrown by the test framework
        }

        verify(podcastGenerationTriggerOutput, never()).send(any(JsonObject.class));
    }
}
