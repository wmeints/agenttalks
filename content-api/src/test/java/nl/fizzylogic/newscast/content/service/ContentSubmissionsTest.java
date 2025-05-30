package nl.fizzylogic.newscast.content.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.SubmissionStatus;

@QuarkusTest
public class ContentSubmissionsTest {
    private ContentSubmissions contentSubmissions;

    @BeforeEach
    public void setUpTestCase() {
        contentSubmissions = new ContentSubmissionsImpl();
    }

    @Test
    @Transactional
    public void canSubmitContent() {
        // Act
        ContentSubmission submission  = contentSubmissions.submitContent("http://localhost:3000");

        // Assert
        assertEquals(SubmissionStatus.SUBMITTED, submission.status);
    }

    @Test
    @Transactional
    public void canSummarizeContent() {
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
        // Act
        ContentSubmission submission = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission processing = contentSubmissions.markForProcessing(submission.id);

        // Assert
        assertEquals(SubmissionStatus.PROCESSING, processing.status);
    }

    @Test
    @Transactional
    public void canMarkContentAsProcessed() {
        // Act
        ContentSubmission submission = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission processed = contentSubmissions.markAsProcessed(submission.id);

        // Assert
        assertEquals(SubmissionStatus.PROCESSED, processed.status);
    }

    @Test
    @Transactional
    public void canFindAllSubmissions() {
        // Act
        ContentSubmission submission1 = contentSubmissions.submitContent("http://localhost:3000");
        ContentSubmission submission2 = contentSubmissions.submitContent("http://localhost:3001");

        // Assert
        var allSubmissions = contentSubmissions.findAll();

        assertTrue(allSubmissions.contains(submission1));
        assertTrue(allSubmissions.contains(submission2));
    }
}
