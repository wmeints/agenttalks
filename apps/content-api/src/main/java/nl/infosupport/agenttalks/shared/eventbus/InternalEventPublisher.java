package nl.infosupport.agenttalks.shared.eventbus;

import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.content.eventbus.ContentSubmissionCreated;

@ApplicationScoped
public class InternalEventPublisher {
    @Inject
    EventBus eventBus;

    public void publishContentSubmissionCreated(ContentSubmissionCreated event) {
        eventBus.publish("content.submission.created", event);
    }
}
