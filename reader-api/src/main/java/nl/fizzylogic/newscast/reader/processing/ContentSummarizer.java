package nl.fizzylogic.newscast.reader.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContentSummarizer {
    @Incoming("content-summarizer-input")
    @Outgoing("content-summarizer-output")
    public Message<JsonObject> process(Message<JsonObject> message) {
        // This class is currently empty, but can be implemented later
        // to handle content summarization logic if needed.
        return message;
    }
}
