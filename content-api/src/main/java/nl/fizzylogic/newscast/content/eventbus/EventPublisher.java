package nl.fizzylogic.newscast.content.eventbus;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventPublisher {
    @Inject
    @Channel("contentSubmissionCreated")
    Emitter<ContentSubmissionCreated> contentSubmissionCreatedEmitter;

    public void publishContentSubmissionCreated(ContentSubmissionCreated event) {
        contentSubmissionCreatedEmitter.send(event);
    }
}