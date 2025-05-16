"""Event definitions for the content API."""

from dataclasses import dataclass
from datetime import datetime


@dataclass
class ContentSubmittedEvent:
    """Event triggered when content is submitted.

    Attributes
    ----------
    id : str
        The ID of the submission.
    url : str
        The URL of the content.
    instructions : str | None
        Instructions for the content.
    created_at : datetime
        The date and time when the content was submitted.
    """

    id: str
    url: str
    instructions: str | None
    created_at: datetime

    def to_dict(self) -> dict[str, any]:
        """Convert the event to a dictionary.

        Returns
        -------
        dict[str, any]
            The event as a dictionary.
        """
        return {
            "id": self.id,
            "url": self.url,
            "instructions": self.instructions,
            "created_at": self.created_at.isoformat(),
        }
