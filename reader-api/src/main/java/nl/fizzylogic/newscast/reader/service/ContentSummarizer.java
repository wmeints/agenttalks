package nl.fizzylogic.newscast.reader.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import nl.fizzylogic.newscast.reader.model.ContentSummary;

@RegisterAiService
public interface ContentSummarizer {
    @SystemMessage(fromResource = "prompts/summarizer/system-instructions.txt")
    @UserMessage(fromResource = "prompts/summarizer/user-instructions.txt")
    ContentSummary summarizeContent(String content);
}
