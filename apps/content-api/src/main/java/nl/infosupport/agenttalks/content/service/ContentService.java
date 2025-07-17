package nl.infosupport.agenttalks.content.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.content.eventbus.ContentSubmissionCreated;
import nl.infosupport.agenttalks.content.eventbus.EventPublisher;
import nl.infosupport.agenttalks.content.graph.errors.ContentSubmissionNotFoundException;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;

@ApplicationScoped
public class ContentService {
    @Inject
    EventPublisher eventPublisher;

    @Transactional
    public ContentSubmission submitContent(String url) {
        ContentSubmission submission = new ContentSubmission(url);
        submission.persistAndFlush();

        eventPublisher.publishContentSubmissionCreated(
                new ContentSubmissionCreated(
                        submission.id,
                        submission.url,
                        submission.dateCreated));

        return submission;
    }

    @Transactional
    public ContentSubmission summarizeContent(Long id, String title, String summary) {
        ContentSubmission submission = ContentSubmission.findById(id);

        if (submission == null) {
            throw new ContentSubmissionNotFoundException(id);
        }

        submission.summarize(title, summary);
        submission.persistAndFlush();

        return submission;
    }

    @Transactional
    public ContentSubmission markForProcessing(Long id) {
        ContentSubmission submission = ContentSubmission.findById(id);

        if (submission == null) {
            throw new ContentSubmissionNotFoundException(id);
        }

        submission.markForProcessing();
        submission.persistAndFlush();

        return submission;
    }

    @Transactional
    public ContentSubmission markAsProcessed(Long id) {
        ContentSubmission submission = ContentSubmission.findById(id);

        if (submission == null) {
            throw new ContentSubmissionNotFoundException(id);
        }

        submission.markAsProcessed();
        submission.persistAndFlush();

        return submission;
    }

    @Transactional
    public PodcastEpisode createPodcastEpisode(String title, String audioFile, String showNotes, String description) {
        var episode = new PodcastEpisode(
                title, audioFile,
                PodcastEpisode.getNextEpisodeNumber(),
                showNotes, description);

        episode.persistAndFlush();

        return episode;
    }

    public List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate) {
        return ContentSubmission.findProcessable().list();
    }

    public List<ContentSubmission> findRecentSubmissions() {
        return ContentSubmission.findRecentlySubmitted().list();
    }

    public List<PodcastEpisode> findAllEpisodes() {
        return PodcastEpisode.findAll().list();
    }

    public ApplicationStatistics getStatistics() {
        var podcasts = PodcastEpisode.count();
        var submissions = ContentSubmission.findRecentlySubmitted().count();

        return new ApplicationStatistics(podcasts, submissions);
    }
}
