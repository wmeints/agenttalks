"""Provides persistence operations for the API.

Data for the API is stored in a MongoDB database. We prefer to use Mongo Atlas to store
the data, but you can also use a local MongoDB instance. The database URL is stored in
an environment variable called `APP_DATABASE_URL`. You can also set the environment
variable `APP_DATABASE_NAME` through the `.env` file.
"""

from datetime import UTC, datetime

from bson import ObjectId
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

    async def find_submissions(
        self, skip: int = 0, limit: int = 20
    ) -> tuple[list[ContentSubmission], int]:
        """Find paginated submissions in the database.

        Parameters
        ----------
        skip : int, optional
            Number of documents to skip, by default 0
        limit : int, optional
            Maximum number of documents to return, by default 20

        Returns
        -------
        tuple[list[ContentSubmission], int]
            A tuple containing the list of submissions and the total count
        """
        # Get total count for pagination
        total = await self._submissions.count_documents({})

        # Get paginated results
        cursor = self._submissions.find().sort("created_at", -1).skip(skip).limit(limit)
        content_submissions = await cursor.to_list(limit)

        return [
            ContentSubmission.from_persistence(submission)
            for submission in content_submissions
        ], total

    async def find_submission_by_id(self, content_id: str) -> ContentSubmission | None:
        """Find a submission by its ID.

        Parameters
        ----------
        content_id : str
            The ID of the submission.

        Returns
        -------
        ContentSubmission
            The submission with the given ID.
        """
        data = await self._submissions.find_one({"_id": ObjectId(content_id)})

        if data is None:
            return None

        return ContentSubmission.from_persistence(data)

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
        if status not in ["pending", "summarizing", "ready", "processed"]:
            exc_message = "Invalid status"
            raise ValueError(exc_message)

        await self._submissions.update_one(
            {"_id": ObjectId(content_id)},
            {"$set": {"status": status, "updated_at": datetime.now(UTC)}},
        )

        return ContentSubmission.from_persistence(
            await self._submissions.find_one({"_id": ObjectId(content_id)})
        )

    async def create_submission(
        self, url: str, instructions: str | None, created_at: datetime
    ) -> ContentSubmission:
        """Create a new submission.

        Parameters
        ----------
        url : str
            The URL of the submission.
        instructions : str | None
            The instructions for the submission.
        created_at : datetime
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
            "updated_at": None,
        }

        result = await self._submissions.insert_one(submission)
        submission = await self._submissions.find_one({"_id": result.inserted_id})

        return ContentSubmission.from_persistence(submission)

    async def update_submission_summary(
        self, content_id: str, summary: str
    ) -> ContentSubmission:
        """Update the summary of a submission.

        Parameters
        ----------
        content_id : str
            The ID of the submission.
        summary : str
            The summary to set.

        Returns
        -------
        ContentSubmission
            The updated submission.
        """
        await self._submissions.update_one(
            {"_id": ObjectId(content_id)},
            {"$set": {"summary": summary, "updated_at": datetime.now(UTC)}},
        )

        return ContentSubmission.from_persistence(
            await self._submissions.find_one({"_id": ObjectId(content_id)})
        )

    async def find_submissions_by_date_range(
        self, start_date: datetime, end_date: datetime
    ) -> list[ContentSubmission]:
        """Find submissions created between the given dates.

        Parameters
        ----------
        start_date : datetime
            The start date (inclusive)
        end_date : datetime
            The end date (inclusive)

        Returns
        -------
        list[ContentSubmission]
            List of submissions created between the given dates
        """
        query = {"created_at": {"$gte": start_date, "$lte": end_date}}

        cursor = self._submissions.find(query).sort("created_at", -1)
        content_submissions = await cursor.to_list(None)

        return [
            ContentSubmission.from_persistence(submission)
            for submission in content_submissions
        ]


def create_submissions_repository() -> SubmissionsRepository:
    """Create a new submissions repository.

    Returns
    -------
    SubmissionsRepository
        A new submissions repository.
    """
    return SubmissionsRepository()
