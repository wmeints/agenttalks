package nl.infosupport.agenttalks.podcast;

import java.time.temporal.ChronoUnit;
import java.util.List;

import nl.infosupport.agenttalks.content.ContentSubmission;
import nl.infosupport.agenttalks.podcast.PodcastHost;
import org.eclipse.microprofile.faulttolerance.Retry;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import nl.infosupport.agenttalks.podcast.PodcastScript;

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