package nl.fizzylogic.newscast.content.service;

import nl.fizzylogic.newscast.content.model.ContentSubmission;

import java.util.List;

public interface ContentSubmissions {
    ContentSubmission submitContent(String url);
    ContentSubmission summarizeContent(long id, String title, String summary);
    ContentSubmission markForProcessing(long id);
    ContentSubmission markAsProcessed(long id);
    List<ContentSubmission> findAll();
}
