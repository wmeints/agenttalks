"""Contains the models used in the API."""

from datetime import datetime

from pydantic import BaseModel, field_validator


class ContentSubmission(BaseModel):
    """Represents a submitted article.

    Attributes
    ----------
    url: str
        The URL for the submitted content
    instructions: str, optional
        The additional instructions
    """

    id: str
    url: str
    instructions: str | None = None
    status: str = "pending"
    created_at: datetime
    updated_at: datetime | None = None

    def is_status_update_valid(self, new_status: str) -> bool:
        """Validate if the new status is valid given the current status.

        Parameters
        ----------
        new_status : str
            The new status to validate.

        Returns
        -------
        bool
            True if the new status is valid, False otherwise.
        """
        valid_statuses = ["pending"]

        if self.status == "summarizing":
            valid_statuses = ["ready"]

        if self.status == "ready":
            valid_statuses = ["processed"]

        if self.status == "pending":
            valid_statuses = ["summarizing"]

        if self.status == "processed":
            valid_statuses = ["processed"]

        return new_status in valid_statuses

    @field_validator("status")
    @classmethod
    def ensure_valid_status(cls, value: str) -> str:
        """Ensure that the status is valid.

        Parameters
        ----------
        value : str
            The status to validate.

        Returns
        -------
        str
            The validated status.
        """
        if value not in ["pending", "summarizing", "ready", "processed"]:
            exc_message = (
                f"Invalid status: {value}. Expected one of: pending, "
                "summarizing, ready, processed"
            )

            raise ValueError(exc_message)

        return value

    @classmethod
    def from_persistence(cls, data: dict) -> "ContentSubmission":
        """Create a ContentSubmission instance from a persistence layer dictionary.

        Parameters
        ----------
        data : dict
            The data from the persistence layer.

        Returns
        -------
        ContentSubmission
            The ContentSubmission instance.
        """
        print("date", type(data["created_at"]))

        return cls(
            id=str(data["_id"]),
            url=data["url"],
            instructions=data.get("instructions"),
            status=data["status"],
            created_at=data["created_at"],
            updated_at=data["updated_at"],
        )
