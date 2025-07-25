package nl.infosupport.agenttalks.podcast;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService

public interface ShowNotesGenerator {
    @UserMessage(fromResource = "prompts/generate-show-notes.md")
    String generateShowNotes(String serializedPodcastScript, String serializedContentSubmissions);
}
