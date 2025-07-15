package nl.infosupport.agenttalks.submissions;

import java.util.List;

import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;

@GraphQLApi
@RequestScoped
public class ContentSubmissionsResource {
        @Inject
    EventBus bus;

    @Query
    @NonNull
    @Description("Finds all content submissions")
    public List<@NonNull ContentSubmission> submissions(int pageIndex, int pageSize) {
        return ContentSubmission.findAll(Sort.by("dateCreated").descending()).page(pageIndex, pageSize).list();
    }

    @Query
    @Transactional
    @Description("Get statistics about the application")
    @NonNull
    public ApplicationStatistics statistics() {
        var podcasts = PodcastEpisode.count();
        var submissions = ContentSubmission.findRecentlySubmitted().count();

        return new ApplicationStatistics(podcasts, submissions);
    }

    @Mutation
    @Transactional
    @Description("Submits content for processing")
    public ContentSubmission submitContent(@Valid SubmitContentRequest input) {
        ContentSubmission submission = new ContentSubmission(input.url, input.instructions);
        submission.persistAndFlush();

        bus.publish("submissions.created", submission);

        return submission;
    }
}
