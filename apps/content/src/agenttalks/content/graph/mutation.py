"""Operations that mutate the content."""

from datetime import UTC, datetime
from typing import Annotated

import strawberry
import strawberry.field_extensions

from agenttalks.content.graph.context import ApplicationContext


@strawberry.type(description="Mutations for the content API.")
class Mutation:
    """Defines the mutations for the content API."""

    @strawberry.mutation(
        description="Set the status of a submission.",
        extensions=[strawberry.field_extensions.InputMutationExtension()],
    )
    async def set_status(
        self,
        info: strawberry.Info[ApplicationContext],
        id: Annotated[  # noqa: A002
            strawberry.ID, strawberry.argument(description="The ID of the submission.")
        ],
        status: Annotated[
            str,
            strawberry.argument(
                description=(
                    "The status to set. Must be one of: "
                    "pending, summarizing, ready, processed"
                )
            ),
        ],
    ) -> None:
        """Set the status of a submission.

        Parameters
        ----------
        id : strawberry.ID
            The ID of the submission.
        status : str
            The status to set.
            Must be one of: pending, summarizing, ready, processed
        """
        await info.context.submissions_repository.update_submission_status(
            str(id), status
        )

    @strawberry.mutation(
        description="Submit content to the application.",
        extensions=[strawberry.field_extensions.InputMutationExtension()],
    )
    async def submit_content(
        self,
        info: strawberry.Info[ApplicationContext],
        url: str,
        instructions: str | None,
    ) -> None:
        """Submit content to the application.

        Parameters
        ----------
        info : strawberry.Info[ApplicationContext]
            The GraphQL info object.
        """
        await info.context.submissions_repository.create_submission(
            url=url, instructions=instructions, created_at=datetime.now(UTC)
        )
