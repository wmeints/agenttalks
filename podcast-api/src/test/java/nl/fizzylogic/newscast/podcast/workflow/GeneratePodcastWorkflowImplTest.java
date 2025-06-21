package nl.fizzylogic.newscast.podcast.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.WorkerFactory;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.shared.TestObjectFactory;

@QuarkusTest
public class GeneratePodcastWorkflowImplTest {
    @Inject
    private WorkflowClient workflowClient;

    @Inject
    WorkerFactory workerFactory;

    @InjectMock
    ContentMetadataActivities contentMetadataActivities;

    @InjectMock
    GeneratePodcastScriptActivities generatePodcastScriptActivities;

    @InjectMock
    GeneratePodcastAudioActivities generatePodcastAudioActivities;

    @InjectMock
    BuzzsproutActivities buzzsproutActivities;

    @Test
    public void canRunWorkflowWithMockedActivities() {
        var script = TestObjectFactory.createPodcastScript();

        when(generatePodcastScriptActivities.generatePodcastScript(any())).thenReturn(script);
        when(generatePodcastAudioActivities.generateSpeech(any())).thenReturn("data/test.mp3");
        when(generatePodcastAudioActivities.concatenateAudioFragments(any())).thenReturn("data/final.mp3");
        when(generatePodcastAudioActivities.mixPodcastEpisode(any())).thenReturn("data/mixed.mp3");
        when(contentMetadataActivities.savePodcastEpisode(any(), any(), any(), any(), any()))
                .thenReturn("https://test.blob.core.windows.net/episodes/test.mp3");
        when(buzzsproutActivities.publishPodcastEpisode(any(), any(), any(), any()))
                .thenReturn("12345");

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue("<default>")
                .setWorkflowRunTimeout(Duration.ofSeconds(2))
                .build();

        var workflowInstance = workflowClient.newWorkflowStub(
                GeneratePodcastWorkflow.class,
                workflowOptions);

        var workflowInput = new GeneratePodcastWorkflowInput(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                List.of(TestObjectFactory.createSummarizedSubmission(),
                        TestObjectFactory.createSummarizedSubmission()));

        workflowInstance.generatePodcast(workflowInput);

        var fragmentsArgumentCapture = ArgumentCaptor.forClass(List.class);
        var lockedContentSubmissions = ArgumentCaptor.forClass(List.class);
        var processedContentSubmissions = ArgumentCaptor.forClass(List.class);

        verify(generatePodcastScriptActivities).generatePodcastScript(any());
        verify(generatePodcastAudioActivities, times(2)).generateSpeech(any());
        verify(generatePodcastAudioActivities).concatenateAudioFragments(fragmentsArgumentCapture.capture());
        verify(generatePodcastAudioActivities).mixPodcastEpisode(any());
        verify(contentMetadataActivities).lockContentSubmissions(lockedContentSubmissions.capture());
        verify(contentMetadataActivities).savePodcastEpisode(any(), any(), any(), any(), any());
        verify(buzzsproutActivities).publishPodcastEpisode(any(), any(), any(), any());
        verify(contentMetadataActivities).markContentSubmissionsAsProcessed(processedContentSubmissions.capture());

        assertEquals(2, fragmentsArgumentCapture.getValue().size(),
                "Expected two audio fragments to be generated for the two podcast hosts in the script.");

        assertEquals(2, lockedContentSubmissions.getValue().size(),
                "Expected two content submissions to be locked for the two podcast hosts in the script.");

        assertEquals(2, processedContentSubmissions.getValue().size(),
                "Expected two content submissions to be marked as processed for the two podcast hosts in the script.");
    }

    @Test
    public void canGeneratePodcastWithProperShowNotesAndDescription() {
        var script = TestObjectFactory.createPodcastScript();

        when(generatePodcastScriptActivities.generatePodcastScript(any())).thenReturn(script);
        when(generatePodcastAudioActivities.generateSpeech(any())).thenReturn("data/test.mp3");
        when(generatePodcastAudioActivities.concatenateAudioFragments(any())).thenReturn("data/final.mp3");
        when(generatePodcastAudioActivities.mixPodcastEpisode(any())).thenReturn("data/mixed.mp3");
        when(contentMetadataActivities.savePodcastEpisode(any(), any(), any(), any(), any()))
                .thenReturn("https://test.blob.core.windows.net/episodes/test.mp3");
        when(buzzsproutActivities.publishPodcastEpisode(any(), any(), any(), any()))
                .thenReturn("12345");

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue("<default>")
                .setWorkflowRunTimeout(Duration.ofSeconds(2))
                .build();

        var workflowInstance = workflowClient.newWorkflowStub(
                GeneratePodcastWorkflow.class,
                workflowOptions);

        // Create test submissions with specific titles and URLs
        var submission1 = TestObjectFactory.createSummarizedSubmission();
        submission1.title = "First Tech Article";
        submission1.url = "https://example.com/first-article";
        
        var submission2 = TestObjectFactory.createSummarizedSubmission();
        submission2.title = "Second Tech Article";
        submission2.url = "https://example.com/second-article";

        var workflowInput = new GeneratePodcastWorkflowInput(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                List.of(submission1, submission2));

        workflowInstance.generatePodcast(workflowInput);

        // Capture the savePodcastEpisode call to verify show notes and description
        var titleCaptor = ArgumentCaptor.forClass(String.class);
        var audioFileCaptor = ArgumentCaptor.forClass(String.class);
        var showNotesCaptor = ArgumentCaptor.forClass(String.class);
        var descriptionCaptor = ArgumentCaptor.forClass(String.class);
        var submissionsCaptor = ArgumentCaptor.forClass(List.class);

        verify(contentMetadataActivities).savePodcastEpisode(
                titleCaptor.capture(),
                audioFileCaptor.capture(),
                showNotesCaptor.capture(),
                descriptionCaptor.capture(),
                submissionsCaptor.capture());

        // Verify show notes content
        String showNotes = showNotesCaptor.getValue();
        assertEquals(true, showNotes.contains("In this episode, we cover:"), 
                "Show notes should contain episode introduction");
        assertEquals(true, showNotes.contains("First Tech Article"), 
                "Show notes should contain first article title");
        assertEquals(true, showNotes.contains("https://example.com/first-article"), 
                "Show notes should contain first article URL");
        assertEquals(true, showNotes.contains("Second Tech Article"), 
                "Show notes should contain second article title");
        assertEquals(true, showNotes.contains("https://example.com/second-article"), 
                "Show notes should contain second article URL");

        // Verify description content
        String description = descriptionCaptor.getValue();
        assertEquals(true, description.contains("This episode covers the latest developments"), 
                "Description should contain episode overview");
        assertEquals(true, description.contains("first tech article"), 
                "Description should contain first article (lowercase)");
        assertEquals(true, description.contains("second tech article"), 
                "Description should contain second article (lowercase)");
    }
}
