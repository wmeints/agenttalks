package nl.infosupport.agenttalks.content.service;

import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;

import java.time.LocalDate;
import java.util.List;

public interface ContentSubmissions {
    ContentSubmission submitContent(String url);

    ContentSubmission summarizeContent(long id, String title, String summary);

    ContentSubmission markForProcessing(long id);

    ContentSubmission markAsProcessed(long id);

    List<ContentSubmission> findAll();

    List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate);

    List<ContentSubmission> findRecentSubmissions();

    PodcastEpisode createPodcastEpisode(String audioFilePath, String title, String showNotes, String description);
}
