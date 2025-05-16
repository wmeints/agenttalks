from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent
from dapr.ext.fastapi import DaprApp
from fastapi import FastAPI, Response

app = FastAPI()
dapr_app = DaprApp(app)

@dapr_app.subscribe(pubsub="eventbus", topic="content.submissions.created.v1")
def handle_submission_created(event: CloudEventEnvelope[SubmissionCreatedEvent]) -> Response:
    print(event)
    return Response(status_code=200)