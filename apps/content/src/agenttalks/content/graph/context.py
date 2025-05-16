"""Context for the GraphQL interface.

This module contains the context for the GraphQL interface. The context contains
additional dependencies needed for the GraphQL interface, such as the repository
interface for the database.

The use of the context is important, because this allows us to replace the real
implementation of various components with mock implementations for testing purposes.

We can inject a mock implementation of the context into the schema in the tests.
This allows us to test the schema without needing to set up a real database or
other dependencies.
"""

from typing import Annotated

from fastapi import Depends
from strawberry.fastapi import BaseContext

from agenttalks.content.eventbus.client import EventPublisher
from agenttalks.content.persistence import (
    SubmissionsRepository,
    create_submissions_repository,
)


class ApplicationContext(BaseContext):
    """Application context for the GraphQL interface."""

    def __init__(
        self,
        *,
        submissions_repository: SubmissionsRepository,
        event_publisher: EventPublisher,
    ) -> None:
        """Initialize the context.

        Parameters
        ----------
        submissions_repository : SubmissionsRepository
            The repository for submissions.
        event_publisher : EventPublisher
            The event publisher for publishing events.
        """
        super().__init__()

        self.submissions_repository = submissions_repository
        self.event_publisher = event_publisher


async def get_application_context(
    submissions_repository: Annotated[
        SubmissionsRepository, Depends(create_submissions_repository)
    ],
) -> ApplicationContext:
    """Get the context for the GraphQL interface.

    Any dependencies needed for the GraphQL interface should be added here
    with an Annotated type hint pointing to the corresponding dependency.

    Please refer to the FastAPI documentation for more information on how to use
    dependency injection with FastAPI:
    https://fastapi.tiangolo.com/tutorial/dependencies/#first-steps

    Parameters
    ----------
    submissions_repository : SubmissionsRepository
        The repository for submissions.

    Returns
    -------
    dict
        The context for the GraphQL interface.
    """
    return ApplicationContext(
        submissions_repository=submissions_repository, event_publisher=EventPublisher()
    )
