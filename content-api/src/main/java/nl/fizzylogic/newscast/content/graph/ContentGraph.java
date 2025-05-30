package nl.fizzylogic.newscast.content.graph;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.content.model.ContentSubmission;
import nl.fizzylogic.newscast.content.graph.input.MarkAsProcessed;
import nl.fizzylogic.newscast.content.graph.input.MarkForProcessing;
import nl.fizzylogic.newscast.content.graph.input.SubmitContent;
import nl.fizzylogic.newscast.content.graph.input.SummarizeContent;
import nl.fizzylogic.newscast.content.service.ContentSubmissions;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
@RequestScoped
public class ContentGraph {
    @Inject
    ContentSubmissions contentSubmissions;

    @Query
    public List<ContentSubmission> submissions() {
        return contentSubmissions.findAll();
    }

    @Mutation
    @Transactional
    public ContentSubmission submitContent(SubmitContent input) {
        return contentSubmissions.submitContent(input.url);
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
