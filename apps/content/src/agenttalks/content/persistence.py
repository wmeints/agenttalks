"""Provides persistence operations for the API.

Data for the API is stored in a MongoDB database. We prefer to use Mongo Atlas to store
the data, but you can also use a local MongoDB instance. The database URL is stored in
an environment variable called `APP_DATABASE_URL`. You can also set the environment
variable `APP_DATABASE_NAME` through the `.env` file.
"""

from typing import List

from pymongo import AsyncMongoClient

from agenttalks.content.config import ApplicationSettings
from agenttalks.content.models import ContentSubmission

settings = ApplicationSettings()


class SubmissionsRepository:
    """Repository for submissions."""

    def __init__(self) -> None:
        """Initialize the repository."""
        self._client = AsyncMongoClient(settings.database_url)
        self._database = self._client.content
        self._submissions = self._database.submissions

    async def find_submissions(self) -> List[ContentSubmission]:
        """Find all submissions in the database.

        Returns
        -------
        List[ContentSubmission]
            A list of all submissions in the database.
        """
        content_submissions = (
            await self._submissions.find().sort("created_at", -1).to_list()
        )

        return [
            ContentSubmission.from_persistence(submission)
            for submission in content_submissions
        ]

    async def update_submission_status(
        self, content_id: str, status: str
    ) -> ContentSubmission:
        """Update the status of a submission.

        Parameters
        ----------
        content_id : str
            The ID of the submission.
        status : str
            The status to set.

        Returns
        -------
        ContentSubmission
            The updated submission.
        """
        await self._submissions.update_one(
            {"_id": content_id}, {"$set": {"status": status}}
        )

        return await self._submissions.find_one({"_id": content_id})

    async def create_submission(
        self, url: str, instructions: str | None, created_at: str
    ) -> ContentSubmission:
        """Create a new submission.

        Parameters
        ----------
        url : str
            The URL of the submission.
        instructions : str | None
            The instructions for the submission.
        created_at : str
            The creation date of the submission.

        Returns
        -------
        ContentSubmission
            The created submission.
        """
        submission = {
            "url": url,
            "instructions": instructions,
            "status": "pending",
            "created_at": created_at,
            "updated_at": created_at,
        }
        result = await self._submissions.insert_one(submission)
        submission = await self._submissions.find_one({"_id": result.inserted_id})

        return ContentSubmission.from_persistence(submission)


def create_submissions_repository() -> SubmissionsRepository:
    """Create a new submissions repository.

    Returns
    -------
    SubmissionsRepository
        A new submissions repository.
    """
    return SubmissionsRepository()
