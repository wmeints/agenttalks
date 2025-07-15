package nl.infosupport.agenttalks.summarization;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.submissions.ContentSubmission;
import nl.infosupport.agenttalks.submissions.SubmissionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ContentProcessorTest {

    @Inject
    ContentProcessor contentProcessor;

    @Inject
    ContentSummarizer contentSummarizer;

    private ContentSubmission testSubmission;
    private final String TEST_URL = "https://example.com/test-article";

    @BeforeEach
    @Transactional
    void setUp() {
        // Create a test submission to use in tests
        testSubmission = new ContentSubmission(TEST_URL, "Test instructions");
        testSubmission.persistAndFlush();
    }

    /**
     * Test the expected case where content is successfully downloaded and summarized.
     * This test uses the real database and real services.
     */
    @Test
    @TestTransaction
    public void testProcessContentSubmission_ExpectedCase() {
        // Act
        contentProcessor.processContentSubmission(testSubmission);

        // Assert
        // Refresh the entity from the database
        ContentSubmission refreshedSubmission = ContentSubmission.findById(testSubmission.id);

        assertNotNull(refreshedSubmission);
        assertEquals(SubmissionStatus.SUMMARIZED, refreshedSubmission.status);
        assertNotNull(refreshedSubmission.title);
        assertNotNull(refreshedSubmission.summary);
        assertNotNull(refreshedSubmission.dateModified);
    }

    /**
     * Test an edge case where the content is empty but still valid.
     * This test uses the real database and real services.
     */
    @Test
    @TestTransaction
    public void testProcessContentSubmission_EdgeCase_EmptyContent() throws IOException {
        // Arrange - Create a submission with a URL that will return empty content
        ContentSubmission emptyContentSubmission = new ContentSubmission(
                "https://example.com/empty-article", 
                "Test instructions for empty content"
        );
        emptyContentSubmission.persistAndFlush();

        // Act
        contentProcessor.processContentSubmission(emptyContentSubmission);

        // Assert
        ContentSubmission refreshedSubmission = ContentSubmission.findById(emptyContentSubmission.id);

        assertNotNull(refreshedSubmission);
        assertEquals(SubmissionStatus.SUMMARIZED, refreshedSubmission.status);
        // Even with empty content, we should still get a title and summary
        assertNotNull(refreshedSubmission.title);
        assertNotNull(refreshedSubmission.summary);
    }

    /**
     * Test a failure case where the content download fails.
     * This test uses the real database and real services.
     */
    @Test
    @TestTransaction
    public void testProcessContentSubmission_FailureCase_DownloadFails() {
        // Arrange - Create a submission with an invalid URL
        ContentSubmission invalidUrlSubmission = new ContentSubmission(
                "https://invalid-url-that-does-not-exist.example", 
                "Test instructions for invalid URL"
        );
        invalidUrlSubmission.persistAndFlush();

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            contentProcessor.processContentSubmission(invalidUrlSubmission);
        });

        assertTrue(exception.getMessage().contains("Content processing failed"));
        assertTrue(exception.getCause() instanceof ContentDownloadFailedException);

        // Verify the submission status was not updated
        ContentSubmission refreshedSubmission = ContentSubmission.findById(invalidUrlSubmission.id);
        assertEquals(SubmissionStatus.SUBMITTED, refreshedSubmission.status);
    }
}
