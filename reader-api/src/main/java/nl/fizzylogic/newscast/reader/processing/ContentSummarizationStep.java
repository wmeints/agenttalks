package nl.fizzylogic.newscast.reader.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.model.ContentSummarizationData;
import nl.fizzylogic.newscast.reader.service.ContentSummarizer;

@ApplicationScoped
public class ContentSummarizationStep {
    @Inject
    ContentSummarizer summarizerAgent;

    @ActivateRequestContext
    @Incoming("content-summarizer-input")
    @Outgoing("content-summarizer-output")
    public ContentSummarizationData process(ContentSummarizationData message) {
        var contentSummary = summarizerAgent.summarizeContent(message.body);
        return message.withSummary(contentSummary.title, contentSummary.summary);
    }
}
