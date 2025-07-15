package nl.infosupport.agenttalks.submissions;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ContentSubmissionsResourceTest {

    @Inject
    ContentSubmissionsResource resource;

    private ContentSubmission testSubmission;

    @BeforeEach
    @Transactional
    void setUp() {
        // Create a test submission to use in tests
        testSubmission = new ContentSubmission("https://example.com/test-article", "Test instructions");
        testSubmission.persistAndFlush();
    }

    @Test
    @TestTransaction
    public void testSubmitContent_ExpectedCase() {
        // Arrange
        SubmitContentRequest request = new SubmitContentRequest("https://example.com/article", "Please summarize this article");

        // Act
        ContentSubmission result = resource.submitContent(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.id);
        assertEquals("https://example.com/article", result.url);
        assertEquals("Please summarize this article", result.instructions);
        assertEquals(SubmissionStatus.SUBMITTED, result.status);
        assertNotNull(result.dateCreated);
    }

    @Test
    @TestTransaction
    public void testSubmissions_EdgeCase_PaginationBoundaries() {
        // Arrange - Create multiple submissions
        for (int i = 0; i < 5; i++) {
            ContentSubmission submission = new ContentSubmission(
                    "https://example.com/article-" + i,
                    "Test instructions " + i
            );
            submission.persistAndFlush();
        }

        // Act & Assert - Test edge cases for pagination
        
        // Case 1: First page, small page size
        List<ContentSubmission> firstPage = resource.submissions(0, 2);
        assertEquals(2, firstPage.size());
        
        // Case 2: Middle page
        List<ContentSubmission> middlePage = resource.submissions(1, 2);
        assertEquals(2, middlePage.size());
        
        // Case 3: Last page (partial)
        List<ContentSubmission> lastPage = resource.submissions(3, 2);
        assertEquals(0, lastPage.size());
        
        // Case 4: Page size larger than total items
        List<ContentSubmission> largePage = resource.submissions(0, 10);
        assertEquals(6, largePage.size()); // 5 created + 1 from setUp
        
        // Case 5: Empty result (page beyond available data)
        List<ContentSubmission> emptyPage = resource.submissions(10, 10);
        assertEquals(0, emptyPage.size());
    }

    @Test
    @TestTransaction
    public void testStatistics() {
        // Arrange - Create a podcast episode
        PodcastEpisode episode = new PodcastEpisode(
                "Test Episode",
                "https://example.com/audio.mp3",
                1,
                "Test show notes",
                "Test description"
        );
        episode.persistAndFlush();

        // Act
        ApplicationStatistics stats = resource.statistics();

        // Assert
        assertEquals(1, stats.totalEpisodes);
        assertEquals(1, stats.submissionsLastWeek); // The one created in setUp
    }

    @Test
    @TestTransaction
    public void testSubmitContent_FailureCase_EmptyUrl() {
        // Arrange
        SubmitContentRequest request = new SubmitContentRequest("", "Please summarize this article");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            resource.submitContent(request);
        });
    }
}