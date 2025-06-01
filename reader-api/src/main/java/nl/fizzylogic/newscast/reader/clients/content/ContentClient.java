package nl.fizzylogic.newscast.reader.clients.content;

import org.eclipse.microprofile.graphql.Mutation;

import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import nl.fizzylogic.newscast.reader.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.reader.clients.content.model.SummarizeContent;

@GraphQLClientApi(configKey = "content-api")
public interface ContentClient {
    @Mutation
    ErrorOr<ContentSubmission> summarizeContent(SummarizeContent input);
}
