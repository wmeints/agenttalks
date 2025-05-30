package nl.fizzylogic.newscast.content.graph;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.graph.input.MarkAsProcessed;
import nl.fizzylogic.newscast.content.graph.input.MarkForProcessing;
import nl.fizzylogic.newscast.content.graph.input.SubmitContent;
import nl.fizzylogic.newscast.content.graph.input.SummarizeContent;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.service.ContentSubmissions;

@QuarkusTest
public class ContentGraphTest {
    private ContentGraph contentGraph;
    private ContentSubmissions contentSubmissions;

    @BeforeEach
    public void setUpTestCase() {
        contentSubmissions = mock(ContentSubmissions.class);
        contentGraph = new ContentGraph();
        contentGraph.contentSubmissions = contentSubmissions;
    }

    @Test
    @Transactional
    public void canSubmitContent() {
        contentGraph.submitContent(new SubmitContent("http://localhost:3000/"));

        verify(contentGraph.contentSubmissions).submitContent(eq("http://localhost:3000/"));
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
}
