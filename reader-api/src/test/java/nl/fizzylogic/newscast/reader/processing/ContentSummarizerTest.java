package nl.fizzylogic.newscast.reader.processing;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.agent.SummarizerAgent;
import nl.fizzylogic.newscast.reader.model.ContentDownload;

@QuarkusTest
public class ContentSummarizerTest {

    @Inject
    ContentSummarizer contentSummarizer;

    @InjectMock
    SummarizerAgent summarizerAgent;

    @Test
    public void canSummarizeContent() {
        when(summarizerAgent.summarizeContent(anyString())).thenReturn(
                Uni.createFrom().item("This is a summary"));

        var contentDownload = new ContentDownload(1L, "text/html", "This is some sample content to summarize");

        contentSummarizer.process(JsonObject.mapFrom(contentDownload))
                .await().atMost(Duration.ofSeconds(10));
    }
}
