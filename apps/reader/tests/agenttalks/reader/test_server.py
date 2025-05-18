from datetime import datetime, timezone
from unittest.mock import patch

import pytest
from fastapi.testclient import TestClient

from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent
from agenttalks.reader.server import app


@pytest.fixture
def mock_submission_created_event():
    payload = SubmissionCreatedEvent(
        id="test-submission-123",
        url="https://example.com/content",
        instructions="Test instructions",
        created_at=datetime.now(timezone.utc),
    )

    return CloudEventEnvelope[SubmissionCreatedEvent](
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


def test_handle_submission_created(mock_submission_created_event):
    with patch("agenttalks.reader.server.wf_client") as mock_wf_client:
        test_client = TestClient(app)

        response = test_client.post(
            "/events/eventbus/content.submissions.created.v1",
            content=mock_submission_created_event.model_dump_json(),
            headers={"Content-Type": "application/cloudevents+json"},
        )

        assert response.status_code == 200

        mock_wf_client.schedule_new_workflow.assert_called_once()
