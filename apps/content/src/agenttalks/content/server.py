"""The entrypoint for the HTTP server."""

from typing import Annotated

from dapr.ext.fastapi import DaprApp
from fastapi import Depends, FastAPI

from agenttalks.content.eventbus.client import EventPublisher, create_event_publisher
from agenttalks.content.models import ContentSubmission
from agenttalks.content.persistence import (
    SubmissionsRepository,
    create_submissions_repository,
)

app = FastAPI()
dapr_app = DaprApp(app)


@app.get("/submissions")
def get_submissions(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
) -> list[ContentSubmission]:
    """Get all submissions."""
    return submissions_repository.find_submissions()


@app.post("/submissions")
def create_submission(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    event_publisher: Annotated[EventPublisher, Depends(create_event_publisher)],
    submission: ContentSubmission,
) -> ContentSubmission:
    """Create a new submission."""
    submission = submissions_repository.create_submission(submission)
    event_publisher.publish_submission_created(submission)

    return submission


@app.put("/submissions/{submission_id}/status")
def set_submission_status(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
    status: str,
) -> ContentSubmission:
    """Set the status of a submission."""
    return submissions_repository.update_submission_status(submission_id, status)


@app.get("/submissions/{submission_id}")
def get_submission(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
) -> ContentSubmission:
    """Get a submission by ID."""
    return submissions_repository.find_submission_by_id(submission_id)


@app.put("/submissions/{submission_id}/summary")
def update_submission_summary(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
    summary: str,
) -> ContentSubmission:
    """Update the summary of a submission."""
    return submissions_repository.update_submission_summary(submission_id, summary)
