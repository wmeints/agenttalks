package nl.fizzylogic.newscast.content.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.eventbus.EventPublisher;
import nl.fizzylogic.newscast.content.graph.input.CreatePodcastEpisode;
import nl.fizzylogic.newscast.content.graph.input.MarkAsProcessed;
import nl.fizzylogic.newscast.content.graph.input.MarkForProcessing;
import nl.fizzylogic.newscast.content.graph.input.SubmitContent;
import nl.fizzylogic.newscast.content.graph.input.SummarizeContent;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.PodcastEpisode;
import nl.fizzylogic.newscast.content.service.ContentSubmissions;

@QuarkusTest
public class ContentGraphTest {
    @Inject
    private ContentGraph contentGraph;

    @InjectMock
    private ContentSubmissions contentSubmissions;

    @InjectMock
    private EventPublisher eventPublisher;

    @Test
    @Transactional
    public void canSubmitContent() {
        ContentSubmission contentSubmission = new ContentSubmission("http://localhost:3000/");
        contentSubmission.id = 1L; // Simulate an ID being assigned

        when(contentSubmissions.submitContent(anyString()))
                .thenReturn(contentSubmission);

        contentGraph.submitContent(new SubmitContent("http://localhost:3000/"));

        verify(contentSubmissions).submitContent(eq("http://localhost:3000/"));
        verify(eventPublisher).publishContentSubmissionCreated(any());
    }

    @Test
    @Transactional
    public void canFindSubmissions() {
        List<ContentSubmission> submissions = List.of(
                new ContentSubmission("http://localhost:3000/"),
                new ContentSubmission("http://localhost:3000/2"));

        when(contentSubmissions.findAll()).thenReturn(submissions);

        List<ContentSubmission> result = contentGraph.submissions();

        assertEquals(2, result.size());
        verify(contentSubmissions).findAll();
    }

    @Test
    @Transactional
    public void canSummarizeContent() {
        ContentSubmission contentSubmission = new ContentSubmission("http://localhost:3000/");

        when(contentSubmissions.summarizeContent(anyLong(), anyString(), anyString()))
                .thenReturn(contentSubmission);

        ContentSubmission result = contentGraph.summarizeContent(
                new SummarizeContent(1L, "test", "test"));

        assertNotNull(result);
        verify(contentSubmissions).summarizeContent(1L, "test", "test");
    }

    @Test
    @Transactional
    public void canMarkContentForProcessing() {
        ContentSubmission contentSubmission = new ContentSubmission("http://localhost:3000/");
        when(contentSubmissions.markForProcessing(anyLong())).thenReturn(contentSubmission);

        ContentSubmission result = contentGraph.markForProcessing(new MarkForProcessing(1L));

        assertEquals(contentSubmission, result);
        verify(contentSubmissions).markForProcessing(eq(1L));
    }

    @Test
    @Transactional
    public void canMarkContentAsProcessed() {
        ContentSubmission contentSubmission = new ContentSubmission("http://localhost:3000/");
        when(contentSubmissions.markAsProcessed(anyLong())).thenReturn(contentSubmission);

        ContentSubmission result = contentGraph.markAsProcessed(new MarkAsProcessed(1L));

        assertEquals(contentSubmission, result);
        verify(contentSubmissions).markAsProcessed(eq(1L));
    }

    @Test
    @Transactional
    public void canCreatePodcastEpisode() {
        // Arrange
        PodcastEpisode expectedEpisode = new PodcastEpisode(
                "Test Episode", 
                "src/test/resources/audio/joop-fragment-01.mp3", 
                1, 
                "Test show notes", 
                "Test description");
        
        when(contentSubmissions.createPodcastEpisode(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectedEpisode);

        CreatePodcastEpisode input = new CreatePodcastEpisode(
                "src/test/resources/audio/joop-fragment-01.mp3", 
                "Test Episode", 
                "Test show notes", 
                "Test description");

        // Act
        PodcastEpisode result = contentGraph.createPodcastEpisode(input);

        // Assert
        assertEquals(expectedEpisode, result);
        verify(contentSubmissions).createPodcastEpisode(
                "src/test/resources/audio/joop-fragment-01.mp3", 
                "Test Episode", 
                "Test show notes", 
                "Test description");
    }
}
