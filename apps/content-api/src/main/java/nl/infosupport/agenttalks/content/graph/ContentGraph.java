package nl.infosupport.agenttalks.content.graph;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.content.graph.input.CreatePodcastEpisode;
import nl.infosupport.agenttalks.content.graph.input.MarkAsProcessed;
import nl.infosupport.agenttalks.content.graph.input.MarkForProcessing;
import nl.infosupport.agenttalks.content.graph.input.SubmitContent;
import nl.infosupport.agenttalks.content.graph.input.SummarizeContent;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;
import nl.infosupport.agenttalks.content.service.ContentService;

@GraphQLApi
@RequestScoped
public class ContentGraph {
    @Inject
    ContentService contentService;

    @Query
    @NonNull
    @Description("Finds all content submissions")
    public List<@NonNull ContentSubmission> submissions(int pageIndex, int pageSize) {
        return ContentSubmission.findAll(Sort.by("dateCreated").descending()).page(pageIndex, pageSize).list();
    }

    @Query
    @Description("Finds all podcast episodes")
    public List<@NonNull PodcastEpisode> episodes() {
        return contentService.findAllEpisodes();
    }

    @Query
    @Description("Finds the content submissions that were created during the week")
    public List<@NonNull ContentSubmission> recentSubmissions() {
        return contentService.findRecentSubmissions();
    }

    @Query
    @Description("Finds processable content submissions for the current week")
    public List<@NonNull ContentSubmission> processableSubmissions() {
        return contentService.findProcessableSubmissions(null, null);
    }

    @Query
    @Description("Get statistics about the application")
    @NonNull
    public ApplicationStatistics statistics() {
        return contentService.getStatistics();
    }

    @Mutation
    @Description("Submits content for processing")
    public ContentSubmission submitContent(SubmitContent input) {
        return contentService.submitContent(input.url);
    }

    @Mutation
    @Description("Creates a new podcast episode")
    public PodcastEpisode createPodcastEpisode(CreatePodcastEpisode input) {
        return contentService.createPodcastEpisode(input.title, input.audioFile, input.showNotes, input.description);
    }

    @Mutation
    @Description("Updates a content submission with a summary")
    public ContentSubmission summarizeContent(SummarizeContent input) {
        return contentService.summarizeContent(input.id, input.title, input.summary);
    }

    @Mutation
    @Description("Marks a content submission for processing")
    public ContentSubmission markForProcessing(MarkForProcessing input) {
        return contentService.markForProcessing(input.id);
    }

    @Mutation
    @Description("Marks a content submission as processed")
    public ContentSubmission markAsProcessed(MarkAsProcessed input) {
        return contentService.markAsProcessed(input.id);
    }
}
