package nl.fizzylogic.newscast.reader.processing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.clients.content.ContentClient;
import nl.fizzylogic.newscast.reader.model.ContentSummary;

@QuarkusTest
public class ContentSubmissionUpdaterTest {
    @InjectMock
    ContentClient contentClient;

    @Inject
    ContentSubmissionUpdater contentSubmissionUpdater;

    @Test
    public void canUpdateContentSubmission() {
        var contentSummary = new ContentSummary(1L, "text/html", "This is a summary");
        contentSubmissionUpdater.updateContentSubmission(JsonObject.mapFrom(contentSummary));

        verify(contentClient).summarizeContent(any());
    }
}
