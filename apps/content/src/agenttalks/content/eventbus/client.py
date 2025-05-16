"""Event bus integration for publishing application events."""

import json

from dapr.clients import DaprClient

from agenttalks.content.eventbus.events import ContentSubmittedEvent
from agenttalks.content.models import ContentSubmission


class EventPublisher:
    """Connects the content API to the event bus.

    We use Dapr pub/sub to publish events to the event bus.
    The event bus is used to communicate with other services in the system.
    """

    def __init__(self, dapr_client: DaprClient | None = None) -> None:
        """Initialize the event publisher."""
        self._client = dapr_client or DaprClient()

    def publish_submission_created(self, submission: ContentSubmission) -> None:
        """Publish a content submission event.

        Parameters
        ----------
        submission : ContentSubmission
            The content submission to publish.
        """
        event_data = ContentSubmittedEvent(
            id=submission.id,
            url=submission.url,
            instructions=submission.instructions,
            created_at=submission.created_at,
        )

        self._client.publish_event(
            pubsub_name="eventbus",
            topic_name="content.submissions.created.v1",
            data=json.dumps(event_data.to_dict()),
        )
