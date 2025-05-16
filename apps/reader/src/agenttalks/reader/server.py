"""Server implementation for the reader API."""

import logging

from dapr.ext.fastapi import DaprApp
from dapr.ext.workflow import DaprWorkflowClient
from fastapi import FastAPI, Response

from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent
from agenttalks.reader.workflow import summarize_submission_workflow

logging.basicConfig(level=logging.INFO)

app = FastAPI()
dapr_app = DaprApp(app)


@dapr_app.subscribe(pubsub="eventbus", topic="content.submissions.created.v1")
def handle_submission_created(
    event: CloudEventEnvelope[SubmissionCreatedEvent],
) -> Response:
    """Handle a submission created event."""
    wf_client = DaprWorkflowClient()

    wf_client.schedule_new_workflow(
        workflow=summarize_submission_workflow,
        instance_id=event.data.id,
        input={
            "url": event.data.url,
            "instructions": event.data.instructions,
            "id": event.data.id,
        },
    )

    return Response(status_code=200)
