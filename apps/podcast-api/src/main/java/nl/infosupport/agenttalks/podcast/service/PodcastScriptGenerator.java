package nl.infosupport.agenttalks.podcast.service;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Retry;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import nl.infosupport.agenttalks.podcast.model.PodcastScript;

@RegisterAiService
public interface PodcastScriptGenerator {
    @Retry(maxRetries = 5, delay = 30, delayUnit = ChronoUnit.SECONDS)
    @SystemMessage(fromResource = "prompts/podcast-script/system-instructions.txt")
    @UserMessage(fromResource = "prompts/podcast-script/user-instructions.txt")
    PodcastScript generatePodcastScript(
            String firstHostName,
            String secondHostName,
            String firstHostStyleInstructions,
            String secondHostStyleInstructions,
            String summarizedContent);
}
