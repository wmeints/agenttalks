package nl.fizzylogic.newscast.reader.processing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.clients.content.ContentClient;
import nl.fizzylogic.newscast.reader.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.reader.model.ContentSummary;

@QuarkusTest
public class ContentSubmissionUpdaterTest {
    @InjectMock
    ContentClient contentClient;

    @Inject
    ContentSubmissionUpdater contentSubmissionUpdater;

    @Test
    public void canUpdateContentSubmission() {
        when(contentClient.summarizeContent(any())).thenReturn(ErrorOr.of(new ContentSubmission()));

        var contentSummary = new ContentSummary(1L, "text/html", "This is a summary");
        contentSubmissionUpdater.updateContentSubmission(JsonObject.mapFrom(contentSummary));

        verify(contentClient).summarizeContent(any());
    }
}
