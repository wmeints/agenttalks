package nl.fizzylogic.newscast.content.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.PodcastEpisode;
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

    @Test
    @Transactional
    public void canCreatePodcastEpisode() {
        // Act
        PodcastEpisode episode = contentSubmissions.createPodcastEpisode(
                "/path/to/audio.mp3", 
                "Test Episode", 
                "These are show notes", 
                "This is a description");

        // Assert
        assertEquals("Test Episode", episode.title);
        assertEquals("/path/to/audio.mp3", episode.audioFilePath);
        assertEquals("These are show notes", episode.showNotes);
        assertEquals("This is a description", episode.description);
        assertTrue(episode.episodeNumber > 0); // Episode number should be assigned and positive
        assertNotNull(episode.dateCreated);
    }

    @Test
    @Transactional
    public void episodeNumbersAreAutoAssigned() {
        // Get the current max episode number to ensure test independence
        int initialMaxEpisodeNumber = PodcastEpisode.find("ORDER BY episodeNumber DESC").firstResultOptional()
                .map(episode -> ((PodcastEpisode) episode).episodeNumber)
                .orElse(0);

        // Act - Create first episode
        PodcastEpisode episode1 = contentSubmissions.createPodcastEpisode(
                "/path/to/audio1.mp3", 
                "Episode 1", 
                "Show notes 1", 
                "Description 1");

        // Act - Create second episode
        PodcastEpisode episode2 = contentSubmissions.createPodcastEpisode(
                "/path/to/audio2.mp3", 
                "Episode 2", 
                "Show notes 2", 
                "Description 2");

        // Assert - Episode numbers should be sequential from the max found
        assertEquals(initialMaxEpisodeNumber + 1, episode1.episodeNumber);
        assertEquals(initialMaxEpisodeNumber + 2, episode2.episodeNumber);
    }
}
