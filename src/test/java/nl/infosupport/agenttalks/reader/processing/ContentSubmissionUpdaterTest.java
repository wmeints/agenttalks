package nl.infosupport.agenttalks.reader.processing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.content.service.ContentService;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.reader.model.ContentSummarizationData;

@QuarkusTest
public class ContentSubmissionUpdaterTest {
    @InjectMock
    ContentService contentService;

    @Inject
    ContentProcessor contentProcessor;

    @Test
    public void canUpdateContentSubmission() {
        when(contentService.summarizeContent(any(), any(), any()))
                .thenReturn(new ContentSubmission("https://example.com/test"));

        var processData = new ContentSummarizationData(
                1L, "http://localhost:3000/", "test", "text/plain");

        processData = processData.withSummary("Test Title", "This is a test summary");

        contentProcessor.updateContentSubmission(processData);

        verify(contentService).summarizeContent(any(), any(), any());
    }
}
