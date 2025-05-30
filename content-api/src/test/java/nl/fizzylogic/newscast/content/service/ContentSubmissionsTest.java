package nl.fizzylogic.newscast.content.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.SubmissionStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ContentSubmissionsTest {
    @Test
    @Transactional
    public void canSubmitContent() {
        // Arrange
        ContentSubmissions contentSubmissions = new ContentSubmissionsImpl();

        // Act
        ContentSubmission submission  = contentSubmissions.submitContent("http://localhost:3000");

        // Assert
        assertEquals(SubmissionStatus.SUBMITTED, submission.status);
    }

    @Test
    @Transactional
    public void canSummarizeContent() {
        // Arrange
        ContentSubmissions contentSubmissions = new ContentSubmissionsImpl();

        // Act
        ContentSubmission submission = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission summarized = contentSubmissions.summarizeContent(submission.id, "test", "test");

        // Assert
        assertEquals(SubmissionStatus.SUMMARIZED, summarized.status);
        assertEquals("test", summarized.summary);
        assertEquals("test", summarized.title);
    }

    @Test
    @Transactional
    public void canMarkContentForProcessing() {
        // Arrange
        ContentSubmissions contentSubmissions = new ContentSubmissionsImpl();

        // Act
        ContentSubmission submission = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission processing = contentSubmissions.markForProcessing(submission.id);

        // Assert
        assertEquals(SubmissionStatus.PROCESSING, processing.status);
    }

    @Test
    @Transactional
    public void canMarkContentAsProcessed() {
        // Arrange
        ContentSubmissions contentSubmissions = new ContentSubmissionsImpl();

        // Act
        ContentSubmission submission = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission processed = contentSubmissions.markAsProcessed(submission.id);

        // Assert
        assertEquals(SubmissionStatus.PROCESSED, processed.status);
    }
}
