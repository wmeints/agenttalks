package nl.infosupport.agenttalks.podcast;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface PodcastScriptGenerator {
    @SystemMessage(fromResource = "prompts/podcast-script/system-instructions.txt")
    @UserMessage(fromResource = "prompts/podcast-script/user-instructions.txt")
    @Timeout(value = 2, unit = ChronoUnit.MINUTES)
    @Retry(maxRetries = 5, delay = 30, delayUnit = ChronoUnit.SECONDS)
    PodcastScript generatePodcastScript(
            String firstHostName,
            String secondHostName,
            String firstHostStyleInstructions,
            String secondHostStyleInstructions,
            String summarizedContent);
}