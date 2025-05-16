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

    url: str
    instructions: str | None = None
    status: str = "pending"
    created_at: datetime
    updated_at: datetime

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
        return cls(
            id=data["_id"],
            url=data["url"],
            instructions=data.get("instructions"),
            status=data["status"],
            created_at=data["created_at"],
            updated_at=data["updated_at"],
        )
