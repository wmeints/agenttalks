package nl.infosupport.agenttalks.content.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;

import java.time.LocalDate;

@QuarkusTest
public class ApplicationStatisticsCalculatorImplTest {
    private ApplicationStatisticsCalculator statisticsCalculator;
    
    @BeforeEach
    public void setUpTestCase() {
        statisticsCalculator = new ApplicationStatisticsCalculatorImpl();
    }
    
    @Test
    @Transactional
    public void testGetApplicationStatistics() {
        // Arrange - Clear existing data to ensure test isolation
        PodcastEpisode.deleteAll();
        ContentSubmission.deleteAll();
        
        // Create some podcast episodes
        PodcastEpisode episode1 = new PodcastEpisode("Test Episode 1", "/path/to/audio1.mp3", 1, "Show notes 1", "Description 1");
        episode1.persistAndFlush();
        
        PodcastEpisode episode2 = new PodcastEpisode("Test Episode 2", "/path/to/audio2.mp3", 2, "Show notes 2", "Description 2");
        episode2.persistAndFlush();
        
        // Create some content submissions
        // One from before this week
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        ContentSubmission oldSubmission = new ContentSubmission("http://example.com/old");
        // Manually set the date to last week
        oldSubmission.dateCreated = lastWeek.atStartOfDay();
        oldSubmission.persistAndFlush();
        
        // Two from this week
        ContentSubmission newSubmission1 = new ContentSubmission("http://example.com/new1");
        newSubmission1.persistAndFlush();
        
        ContentSubmission newSubmission2 = new ContentSubmission("http://example.com/new2");
        newSubmission2.persistAndFlush();
        
        // Act
        ApplicationStatistics statistics = statisticsCalculator.getApplicationStatistics();
        
        // Assert
        assertNotNull(statistics);
        assertEquals(2, statistics.totalEpisodes);
        assertEquals(2, statistics.submissionsLastWeek);
    }
    
    @Test
    @Transactional
    public void testGetApplicationStatisticsWithNoData() {
        // Arrange - Clear existing data
        PodcastEpisode.deleteAll();
        ContentSubmission.deleteAll();
        
        // Act
        ApplicationStatistics statistics = statisticsCalculator.getApplicationStatistics();
        
        // Assert
        assertNotNull(statistics);
        assertEquals(0, statistics.totalEpisodes);
        assertEquals(0, statistics.submissionsLastWeek);
    }
    
    @Test
    @Transactional
    public void testGetApplicationStatisticsWithOnlyEpisodes() {
        // Arrange - Clear existing data
        PodcastEpisode.deleteAll();
        ContentSubmission.deleteAll();
        
        // Create some podcast episodes
        PodcastEpisode episode = new PodcastEpisode("Test Episode", "/path/to/audio.mp3", 1, "Show notes", "Description");
        episode.persist();
        
        // Act
        ApplicationStatistics statistics = statisticsCalculator.getApplicationStatistics();
        
        // Assert
        assertNotNull(statistics);
        assertEquals(1, statistics.totalEpisodes);
        assertEquals(0, statistics.submissionsLastWeek);
    }
    
    @Test
    @Transactional
    public void testGetApplicationStatisticsWithOnlySubmissions() {
        // Arrange - Clear existing data
        PodcastEpisode.deleteAll();
        ContentSubmission.deleteAll();
        
        // Create a content submission from this week
        ContentSubmission submission = new ContentSubmission("http://example.com/test");
        submission.persist();
        
        // Act
        ApplicationStatistics statistics = statisticsCalculator.getApplicationStatistics();
        
        // Assert
        assertNotNull(statistics);
        assertEquals(0, statistics.totalEpisodes);
        assertEquals(1, statistics.submissionsLastWeek);
    }
}