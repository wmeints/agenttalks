package nl.fizzylogic.newscast.content.service;

import java.time.LocalDate;
import java.util.List;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.PodcastEpisode;

@RequestScoped
public class ContentSubmissionsImpl implements ContentSubmissions {
    @Override
    public ContentSubmission submitContent(String url) {
        var submission = new ContentSubmission(url);
        submission.persistAndFlush();

        return submission;
    }

    @Override
    public ContentSubmission summarizeContent(long id, String title, String summary) {
        ContentSubmission submission = ContentSubmission.findById(id);

        submission.summarize(title, summary);
        submission.persistAndFlush();

        return submission;
    }

    @Override
    public ContentSubmission markForProcessing(long id) {
        ContentSubmission submission = ContentSubmission.findById(id);

        submission.markForProcessing();
        submission.persistAndFlush();

        return submission;
    }

    @Override
    public ContentSubmission markAsProcessed(long id) {
        ContentSubmission submission = ContentSubmission.findById(id);

        submission.markAsProcessed();
        submission.persistAndFlush();

        return submission;
    }

    @Override
    public List<ContentSubmission> findAll() {
        return ContentSubmission.findAll(Sort.descending("dateCreated")).list();
    }

    @Override
    public List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate) {
        return ContentSubmission.findProcessable(
                startDate, endDate);
    }

    @Override
    public PodcastEpisode createPodcastEpisode(String audioFilePath, String title) {
        var episode = new PodcastEpisode(title, audioFilePath);
        episode.persistAndFlush();

        return episode;
    }
}
