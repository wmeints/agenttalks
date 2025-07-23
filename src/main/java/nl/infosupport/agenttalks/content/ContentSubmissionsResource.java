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
import jakarta.transaction.Transactional;

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

    @Query
    @NonNull
    @Description("Finds all pending content submissions")
    public List<ContentSubmission> pendingSubmissions() {
        return ContentSubmission.findPending().list();
    }

    @Mutation
    @Transactional
    @Description("Submits content for processing")
    public ContentSubmission submitContent(@NonNull SubmitContentRequest input) {
        var submission = new ContentSubmission(input.url, input.instructions);
        submission.persistAndFlush();

        eventBus.publish("submissions.created", submission);

        return submission;
    }
}
