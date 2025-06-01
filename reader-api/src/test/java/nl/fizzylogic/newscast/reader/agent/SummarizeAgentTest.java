package nl.fizzylogic.newscast.reader.agent;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class SummarizeAgentTest {
    @Inject
    SummarizerAgent summarizerAgent;

    @Test
    public void canSummarizeContent() {
        var response = summarizerAgent
                .summarizeContent("This is some sample content to summarize")
                .await()
                .atMost(Duration.ofSeconds(25));

        assertNotNull(response);
    }
}
