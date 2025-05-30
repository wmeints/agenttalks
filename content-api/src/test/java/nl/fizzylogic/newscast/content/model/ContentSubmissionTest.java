package nl.fizzylogic.newscast.content.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ContentSubmissionTest {
    @Test
    public void canCreateContentSubmission() {
        var submission = new ContentSubmission("http://localhost:3000");

        assertEquals("http://localhost:3000", submission.url);
        assertNotNull(submission.dateCreated);
        assertNull(submission.dateModified);
    }

    @Test
    public void canSummarize() {
        var submission = new ContentSubmission("http://localhost:3000");
        submission.summarize("test", "test");

        assertEquals("test", submission.title);
        assertEquals("test", submission.summary);
    }

    @Test
    public void canMarkForProcessing() {
        var submission = new ContentSubmission("http://localhost:3000");
        submission.summarize("test", "test");
        submission.markForProcessing();

        assertEquals(SubmissionStatus.PROCESSING, submission.status);
    }

    @Test
    public void canMarkAsProcessed() {
        var submission = new ContentSubmission("http://localhost:3000");
        submission.summarize("test", "test");
        submission.markAsProcessed();

        assertEquals(SubmissionStatus.PROCESSED, submission.status);
    }
}
