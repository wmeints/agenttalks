package nl.infosupport.agenttalks.podcast;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface EpisodeSummaryGenerator {
    @UserMessage(fromResource = "prompts/generate-episode-summary.md")
    String generateEpisodeDescription(String serializedPodcastScript, String serializedContentSubmissions);
}
