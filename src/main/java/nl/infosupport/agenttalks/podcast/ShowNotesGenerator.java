package nl.infosupport.agenttalks.podcast;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface ShowNotesGenerator {
    @SystemMessage("You're the editor for AgentTalks.")
    @UserMessage(fromResource = "prompts/generate-show-notes.md")
    @Timeout(value = 2, unit = ChronoUnit.MINUTES)
    @Retry(maxRetries = 5, delay = 30, delayUnit = ChronoUnit.SECONDS)
    String generateShowNotes(String serializedPodcastScript, String serializedContentSubmissions);
}
