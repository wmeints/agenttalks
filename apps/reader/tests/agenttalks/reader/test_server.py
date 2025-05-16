from datetime import datetime, timezone

import pytest
from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent
from agenttalks.reader.server import app
from fastapi.testclient import TestClient


@pytest.fixture
def mock_submission_created_event():
    payload = SubmissionCreatedEvent(
        id="test-submission-123",
        url="https://example.com/content",
        instructions="Test instructions",
        created_at=datetime.now(timezone.utc),
    )

    envelope = CloudEventEnvelope[SubmissionCreatedEvent](
        data=payload,
        datacontenttype="application/json",
        id="test-event-id",
        pubsubname="eventbus",
        source="test-source",
        specversion="1.0",
        time=datetime.now(timezone.utc).isoformat(),
        topic="content.submissions.created.v1",
        traceid="",
        traceparent="",
        tracestate="",
        type="com.example.test",
    )

    return envelope


def test_handle_submission_created(mock_submission_created_event):
    """Test that the submission handler returns a 200 status code."""

    test_client = TestClient(app)

    response = test_client.post(
        "/events/eventbus/content.submissions.created.v1",
        content=mock_submission_created_event.model_dump_json(),
        headers={"Content-Type": "application/cloudevents+json"},
    )

    # Check the response
    assert response.status_code == 200
