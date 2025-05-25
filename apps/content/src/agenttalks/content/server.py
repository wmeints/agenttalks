"""The entrypoint for the HTTP server."""

from datetime import UTC, datetime
from typing import Annotated

from dapr.ext.fastapi import DaprApp
from fastapi import Depends, FastAPI, HTTPException, Query

from agenttalks.content.eventbus.client import EventPublisher, create_event_publisher
from agenttalks.content.forms import (
    CreateSubmissionForm,
    UpdateStatusForm,
    UpdateSummaryForm,
)
from agenttalks.content.models import (
    ContentSubmission,
    PaginatedResponse,
    SubmissionListItem,
)
from agenttalks.content.persistence import (
    SubmissionsRepository,
    create_submissions_repository,
)
from agenttalks.content.telemetry import get_tracer

app = FastAPI()
dapr_app = DaprApp(app)
tracer = get_tracer(__name__)


@app.get("/submissions")
async def get_submissions(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    page: int = Query(1, ge=1, description="Page number, starting from 1"),
) -> PaginatedResponse[ContentSubmission]:
    """Get paginated list of submissions.

    Parameters
    ----------
    page : int, optional
        Page number (1-based), by default 1
    """
    # Fix page size to 20 as requested
    page_size = 20
    skip = (page - 1) * page_size

    items, total = await submissions_repository.find_submissions(
        skip=skip, limit=page_size
    )

    total_pages = (total + page_size - 1) // page_size

    return PaginatedResponse[ContentSubmission](
        items=items,
        total=total,
        page=page,
        page_size=page_size,
        total_pages=total_pages,
    )


@app.get("/submissions/by-date")
async def get_submissions_by_date_range(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
    start_date: Annotated[datetime, Query(description="Start date (inclusive)")],
    end_date: Annotated[datetime, Query(description="End date (inclusive)")],
) -> list[SubmissionListItem]:
    """Get a list of submissions between start and end date.

    Parameters
    ----------
    start_date : datetime
        The start date (inclusive)
    end_date : datetime
        The end date (inclusive)

    Returns
    -------
    list[SubmissionListItem]
        List of submissions between the specified dates
    """
    submissions = await submissions_repository.find_submissions_by_date_range(
        start_date, end_date
    )

    return [
        SubmissionListItem(
            id=submission.id,
            url=submission.url,
            summary=submission.summary,
            created_at=submission.created_at,
        )
        for submission in submissions
    ]


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
