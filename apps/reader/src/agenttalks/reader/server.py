"""Server implementation for the reader API."""

import logging

from dapr.ext.fastapi import DaprApp
from dapr.ext.workflow import DaprWorkflowClient
from fastapi import FastAPI, Response

from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent
from agenttalks.reader.workflow import (
    SummarizeSubmissionWorkflowInput,
    summarize_submission_workflow,
)

logging.basicConfig(level=logging.INFO)

app = FastAPI()
dapr_app = DaprApp(app)
wf_client = DaprWorkflowClient()


@dapr_app.subscribe(pubsub="eventbus", topic="content.submissions.created.v1")
def handle_submission_created(
    event: CloudEventEnvelope[SubmissionCreatedEvent],
) -> Response:
    """Handle a submission created event."""
    print("Traceparent: ", event.traceparent)

    wf_client.schedule_new_workflow(
        workflow=summarize_submission_workflow,
        instance_id=event.data.id,
        input=SummarizeSubmissionWorkflowInput(
            content_id=event.data.id,
            url=event.data.url,
            instructions=event.data.instructions,
        ),
    )

    return Response(status_code=200)
