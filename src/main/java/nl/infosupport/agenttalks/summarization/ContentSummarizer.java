package nl.infosupport.agenttalks.summarization;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface ContentSummarizer {
    @SystemMessage(fromResource = "prompts/summarizer/system-instructions.txt")
    @UserMessage(fromResource = "prompts/summarizer/user-instructions.txt")
    ContentSummary summarizeContent(String content);
}
