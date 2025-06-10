package nl.fizzylogic.newscast.content.service;

import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.PodcastEpisode;

import java.time.LocalDate;
import java.util.List;

public interface ContentSubmissions {
    ContentSubmission submitContent(String url);

    ContentSubmission summarizeContent(long id, String title, String summary);

    ContentSubmission markForProcessing(long id);

    ContentSubmission markAsProcessed(long id);

    List<ContentSubmission> findAll();

    List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate);

    PodcastEpisode createPodcastEpisode(String audioFilePath, String title, String showNotes, String description);
}
