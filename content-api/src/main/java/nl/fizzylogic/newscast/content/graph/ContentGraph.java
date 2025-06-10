package nl.fizzylogic.newscast.content.graph;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.eventbus.ContentSubmissionCreated;
import nl.fizzylogic.newscast.content.eventbus.EventPublisher;
import nl.fizzylogic.newscast.content.graph.input.CreatePodcastEpisode;
import nl.fizzylogic.newscast.content.graph.input.MarkAsProcessed;
import nl.fizzylogic.newscast.content.graph.input.MarkForProcessing;
import nl.fizzylogic.newscast.content.graph.input.SubmitContent;
import nl.fizzylogic.newscast.content.graph.input.SummarizeContent;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.model.PodcastEpisode;
import nl.fizzylogic.newscast.content.service.ContentSubmissions;

@GraphQLApi
@RequestScoped
public class ContentGraph {
    @Inject
    ContentSubmissions contentSubmissions;

    @Inject
    EventPublisher eventPublisher;

    @Query
    public List<ContentSubmission> submissions() {
        return contentSubmissions.findAll();
    }

    @Query
    public List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate) {
        return contentSubmissions.findProcessableSubmissions(startDate, endDate);
    }

    @Mutation
    @Transactional
    public ContentSubmission submitContent(SubmitContent input) {
        ContentSubmission submission = contentSubmissions.submitContent(input.url);

        eventPublisher.publishContentSubmissionCreated(
                new ContentSubmissionCreated(
                        submission.id,
                        submission.url,
                        submission.dateCreated));

        return submission;
    }

    @Mutation
    @Transactional
    public PodcastEpisode createPodcastEpisode(CreatePodcastEpisode input) {
        return contentSubmissions.createPodcastEpisode(input.audioFile, input.title, input.showNotes, input.description);
    }

    @Mutation
    @Transactional
    public ContentSubmission summarizeContent(SummarizeContent input) {
        return contentSubmissions.summarizeContent(input.id, input.title, input.summary);
    }

    @Mutation
    @Transactional
    public ContentSubmission markForProcessing(MarkForProcessing input) {
        return contentSubmissions.markForProcessing(input.id);
    }

    @Mutation
    @Transactional
    public ContentSubmission markAsProcessed(MarkAsProcessed input) {
        return contentSubmissions.markAsProcessed(input.id);
    }
}
