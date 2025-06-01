package nl.fizzylogic.newscast.reader.agent;

import io.smallrye.mutiny.Uni;

public interface SummarizerAgent {
    Uni<String> summarizeContent(String content);
}
