"""Event bus integration for publishing application events."""

import json
import logging

from dapr.clients import DaprClient
from opentelemetry import trace

from agenttalks.content.eventbus.events import (
    SubmissionCreatedEvent,
)
from agenttalks.content.models import ContentSubmission
from agenttalks.content.telemetry import get_current_traceparent

logger = logging.getLogger(__name__)


class EventPublisher:
    """Connects the content API to the event bus.

    We use Dapr pub/sub to publish events to the event bus.
    The event bus is used to communicate with other services in the system.
    """

    def __init__(self, dapr_client: DaprClient | None = None) -> None:
        """Initialize the event publisher."""
        self._client = dapr_client

    def publish_submission_created(self, submission: ContentSubmission) -> None:
        """Publish a content submission event.

        Parameters
        ----------
        submission : ContentSubmission
            The content submission to publish.
        """
        if self._client is None:
            self._client = DaprClient()

        event_data = SubmissionCreatedEvent(
            id=submission.id,
            url=submission.url,
            instructions=submission.instructions,
            created_at=submission.created_at,
        )

        self._client.publish_event(
            pubsub_name="eventbus",
            topic_name="content.submissions.created.v1",
            data=json.dumps(event_data.to_dict()),
            data_content_type="application/json",
            publish_metadata={
                "cloudevent.traceparent": get_current_traceparent(),
            },
        )


def create_event_publisher() -> EventPublisher:
    """Create an event publisher."""
    return EventPublisher()
