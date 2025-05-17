"""The entrypoint for the HTTP server."""

from datetime import UTC, datetime
from typing import Annotated

from dapr.ext.fastapi import DaprApp
from fastapi import Depends, FastAPI, HTTPException

from agenttalks.content.eventbus.client import EventPublisher, create_event_publisher
from agenttalks.content.forms import (
    CreateSubmissionForm,
    UpdateStatusForm,
    UpdateSummaryForm,
)
from agenttalks.content.models import ContentSubmission
from agenttalks.content.persistence import (
    SubmissionsRepository,
    create_submissions_repository,
)

app = FastAPI()
dapr_app = DaprApp(app)


@app.get("/submissions")
async def get_submissions(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
) -> list[ContentSubmission]:
    """Get all submissions."""
    return await submissions_repository.find_submissions()


@app.post("/submissions")
async def create_submission(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    event_publisher: Annotated[EventPublisher, Depends(create_event_publisher)],
    input_data: CreateSubmissionForm,
) -> ContentSubmission:
    """Create a new submission."""
    submission = await submissions_repository.create_submission(
        input_data.url, input_data.instructions, datetime.now(UTC)
    )

    event_publisher.publish_submission_created(submission)

    return submission


@app.put("/submissions/{submission_id}/status")
async def set_submission_status(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
    input_data: UpdateStatusForm,
) -> ContentSubmission:
    """Set the status of a submission."""
    submission = await submissions_repository.find_submission_by_id(submission_id)

    if submission is None:
        raise HTTPException(status_code=404, detail="Submission not found")

    if not submission.is_status_update_valid(input_data.status):
        raise HTTPException(status_code=400, detail="Invalid status update")

    return await submissions_repository.update_submission_status(
        submission_id, input_data.status
    )


@app.get("/submissions/{submission_id}")
async def get_submission(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
) -> ContentSubmission:
    """Get a submission by ID."""
    submission = await submissions_repository.find_submission_by_id(submission_id)

    if submission is None:
        raise HTTPException(status_code=404, detail="Submission not found")

    return submission


@app.put("/submissions/{submission_id}/summary")
async def update_submission_summary(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    submission_id: str,
    input_data: UpdateSummaryForm,
) -> ContentSubmission:
    """Update the summary of a submission."""
    submission = await submissions_repository.find_submission_by_id(submission_id)

    if submission is None:
        raise HTTPException(status_code=404, detail="Submission not found")

    return await submissions_repository.update_submission_summary(
        submission_id, input_data.summary
    )
