package nl.fizzylogic.newscast.reader.clients.content;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import nl.fizzylogic.newscast.reader.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.reader.clients.content.model.SummarizeContent;

@GraphQLClientApi(configKey = "content-api")
public interface ContentClient {
    ContentSubmission summarizeContent(SummarizeContent input);
}
