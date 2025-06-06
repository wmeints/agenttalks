package nl.fizzylogic.newscast.podcast.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface PodcastScriptGenerator {
    @SystemMessage(fromResource = "prompts/prompt-script/system-instructions.txt")
    @UserMessage(fromResource = "prompts/prompt-script/user-instructions.txt")
    String generatePodcastScript(
            String firstHostName,
            String secondHostName,
            String firstHostStyleInstructions,
            String secondHostStyleInstructions,
            String summarizedContent);
}
