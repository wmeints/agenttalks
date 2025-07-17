package nl.infosupport.agenttalks.content.eventbus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.shared.eventbus.InternalEventPublisher;

@ApplicationScoped
public class EventPublisher {
    @Inject
    InternalEventPublisher internalEventPublisher;

    public void publishContentSubmissionCreated(ContentSubmissionCreated event) {
        internalEventPublisher.publishContentSubmissionCreated(event);
    }
}