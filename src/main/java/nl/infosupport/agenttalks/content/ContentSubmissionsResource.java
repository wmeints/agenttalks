package nl.infosupport.agenttalks.content;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.panache.common.Sort;
import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;

@GraphQLApi
public class ContentSubmissionsResource {
    @Inject
    EventBus eventBus;

    @Query
    @NonNull
    @Description("Finds all content submissions")
    public ContentSubmissionResultSet submissions(int pageIndex, int pageSize) {
        List<ContentSubmission> items = ContentSubmission.findAll(Sort.by("dateCreated").descending())
                .page(pageIndex, pageSize)
                .list();

        var totalItems = (int) ContentSubmission.count();

        return new ContentSubmissionResultSet(items, totalItems, pageIndex, pageSize);
    }

    @Mutation
    @Description("Submits content for processing")
    public ContentSubmission submitContent(SubmitContentRequest input) {
        var submission = new ContentSubmission(input.url, input.instructions);
        submission.persistAndFlush();

        eventBus.publish("submissions.created", submission);

        return submission;
    }
}
