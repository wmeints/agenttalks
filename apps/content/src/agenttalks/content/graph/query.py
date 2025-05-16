"""Operations that query the content."""

from typing import List

import strawberry

from agenttalks.content.graph.context import ApplicationContext
from agenttalks.content.models import ContentSubmission


@strawberry.experimental.pydantic.type(
    model=ContentSubmission, description="A submission made to the application."
)
class ContentSubmissionType:
    """Type mapping for the `ContentSubmission` model."""

    id: strawberry.ID
    url: strawberry.auto
    instructions: strawberry.auto
    status: strawberry.auto
    created_at: strawberry.auto
    updated_at: strawberry.auto


@strawberry.type(description="Queries for the content API.")
class Query:
    """Defines the queries for the content API."""

    @strawberry.field(description="Submissions made to the application.")
    async def submissions(
        self, info: strawberry.Info[ApplicationContext]
    ) -> List[ContentSubmissionType]:
        """Get all submissions.

        Parameters
        ----------
        info : strawberry.Info[ApplicationContext]
            The GraphQL info object.

        Returns
        -------
        List[ContentSubmissionType]
            A list of all submissions.
        """
        submissions = await info.context.submissions_repository.find_submissions()
        return [
            ContentSubmission.from_persistence(submission) for submission in submissions
        ]
