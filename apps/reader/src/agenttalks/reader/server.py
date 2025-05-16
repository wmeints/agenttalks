from dapr.ext.fastapi import DaprApp
from fastapi import FastAPI, Response
from agenttalks.reader.eventbus.events import CloudEventEnvelope, SubmissionCreatedEvent

app = FastAPI()
dapr = DaprApp(app)

@dapr.subscribe(pubsub="eventbus", topic="content.submissions.created.v1")
def handle_submission_created(event: CloudEventEnvelope[SubmissionCreatedEvent]) -> Response:
    print(event)
    return Response(status_code=200)