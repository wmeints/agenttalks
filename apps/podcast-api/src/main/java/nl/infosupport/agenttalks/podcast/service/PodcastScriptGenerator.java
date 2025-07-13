package nl.infosupport.agenttalks.podcast.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import nl.infosupport.agenttalks.podcast.model.PodcastScript;

@RegisterAiService
public interface PodcastScriptGenerator {
    @SystemMessage(fromResource = "prompts/podcast-script/system-instructions.txt")
    @UserMessage(fromResource = "prompts/podcast-script/user-instructions.txt")
    PodcastScript generatePodcastScript(
            String firstHostName,
            String secondHostName,
            String firstHostStyleInstructions,
            String secondHostStyleInstructions,
            String summarizedContent);
}
