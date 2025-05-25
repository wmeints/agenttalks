from datetime import datetime
from unittest.mock import MagicMock

import pytest
from callee import String

from agenttalks.content.eventbus.client import EventPublisher
from agenttalks.content.models import ContentSubmission


@pytest.fixture
def mock_submission():
    return ContentSubmission(
        id="sub-123",
        url="https://example.com/content",
        instructions="Summarize this content.",
        created_at=datetime.fromisoformat("2024-05-16T12:00:00+00:00"),
        updated_at=datetime.fromisoformat("2024-05-16T12:00:00+00:00"),
    )


@pytest.fixture
def mock_dapr_client():
    """Mock the DaprClient for testing."""
    mock_dapr_client = MagicMock()
    mock_dapr_client.publish_event = MagicMock()
    mock_dapr_client.publish_event.return_value = None
    return mock_dapr_client


def test_publish_content_submitted_translates_event_data(
    mock_submission, mock_dapr_client
):
    publisher = EventPublisher(mock_dapr_client)
    publisher.publish_submission_created(mock_submission)

    mock_dapr_client.publish_event.assert_called_once_with(
        pubsub_name="eventbus",
        topic_name="content.submissions.created.v1",
        data=String(),
        data_content_type='application/json',
        publish_metadata={'cloudevent.traceparent': None}
    )
