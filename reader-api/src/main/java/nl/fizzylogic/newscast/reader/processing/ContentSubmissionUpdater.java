package nl.fizzylogic.newscast.reader.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.graphql.client.GraphQLClientException;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.clients.content.ContentClient;
import nl.fizzylogic.newscast.reader.clients.content.model.SummarizeContent;
import nl.fizzylogic.newscast.reader.model.ContentSummary;

@ApplicationScoped
public class ContentSubmissionUpdater {
    @Inject
    ContentClient contentClient;

    Logger logger = Logger.getLogger(ContentSubmissionUpdater.class);

    @Incoming("content-submission-updater-input")
    public void updateContentSubmission(JsonObject message) {
        var contentSummary = message.mapTo(ContentSummary.class);

        logger.infof(
                "Updating content submission with ID: %d",
                contentSummary.contentSubmissionId);

        try {
            SummarizeContent input = new SummarizeContent(
                    contentSummary.contentSubmissionId,
                    "empty-title",
                    contentSummary.summary);

            var response = contentClient.summarizeContent(input);

            if (response.hasErrors()) {
                throw new RuntimeException("Failed to update content submission");
            }
        } catch (GraphQLClientException ex) {
            logger.errorf("Failed to update content submission: %s", ex.getMessage());
            throw ex;
        }
    }
}
