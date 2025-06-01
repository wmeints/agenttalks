package nl.fizzylogic.newscast.reader.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.agent.SummarizerAgent;
import nl.fizzylogic.newscast.reader.model.ContentDownload;
import nl.fizzylogic.newscast.reader.model.ContentSummary;

@ApplicationScoped
public class ContentSummarizer {
    @Inject
    SummarizerAgent summarizerAgent;

    Logger logger = Logger.getLogger(ContentSummarizer.class);

    @Incoming("content-summarizer-input")
    @Outgoing("content-summarizer-output")
    public Uni<JsonObject> process(JsonObject message) {
        var contentDownload = message.mapTo(ContentDownload.class);

        logger.info(String.format(
                "Summarizing content for submission ID: %d",
                contentDownload.contentSubmissionId));

        var agentResponse = summarizerAgent.summarizeContent(contentDownload.body);

        return agentResponse
                .onItem()
                .transform(responseText -> new ContentSummary(
                        contentDownload.contentSubmissionId,
                        contentDownload.contentType, responseText))
                .onItem()
                .transform(summary -> JsonObject.mapFrom(summary));
    }
}
