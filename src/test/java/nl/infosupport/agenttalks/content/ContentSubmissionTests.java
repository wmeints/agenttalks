package nl.infosupport.agenttalks.content;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ContentSubmissionTests {
    @Test
    public void testCanSummarizeContent() {
        var submission = new ContentSubmission("https://www.google.com", null);
        submission.summarize("Test", "Summary");

        assertEquals("Test", submission.title);
        assertEquals("Summary", submission.summary);
    }

    @Test
    public void testCanMarkForProcessing() {
        var submission = new ContentSubmission("https://www.google.com", null);
        submission.summarize("Test", "Summary");
        submission.markForProcessing();

        assertEquals(SubmissionStatus.PROCESSING, submission.status);
    }

    @Test
    public void testCantMarkSubmittedContent() {
        var submission = new ContentSubmission("https://www.google.com", null);
        assertThrows(IllegalStateException.class, submission::markForProcessing);
    }

    @Test
    public void testCanMarkProcessedContent() {
        var submission = new ContentSubmission("https://www.google.com", null);
        submission.summarize("Test", "Summary");
        submission.markForProcessing();
        submission.markAsProcessed();

        assertEquals(SubmissionStatus.PROCESSED, submission.status);
    }

    @Test
    public void testCantMarkAsProcessedUnprocessedContent() {
        var submission = new ContentSubmission("https://www.google.com", null);
        submission.summarize("Test", "Summary");

        assertThrows(IllegalStateException.class, submission::markAsProcessed);
    }

    @Test
    @TestTransaction
    public void testCanFindPendingSubmissions() {
        var submission = new ContentSubmission("https://www.google.com", null);
        submission.summarize("Test", "Summary");
        submission.persistAndFlush();

        assertEquals(1, ContentSubmission.findPending().count());
    }
}
