"""Event definitions for the reader API."""

from dataclasses import dataclass
from datetime import datetime
from typing import TypeVar

from pydantic import BaseModel

TEvent = TypeVar("TEvent", bound="BaseModel")


class CloudEventEnvelope[TEvent](BaseModel):
    """Data structure for incoming events that are published as cloud events.

    This data model is based on the cloudevents specification:
    https://github.com/cloudevents/spec/blob/v1.0.2/cloudevents/spec.md

    Attributes
    ----------
    data : T
        The data payload of the event.
    datacontenttype : str
        The content type of the data.
    id : str
        The unique identifier for the event.
    pubsubname : str
        The name of the pub/sub system that published the event.
    source : str
        The source of the event.
    specversion : str
        The version of the CloudEvents specification.
    topic : str
        The topic of the event.
    traceparent : str
        The traceparent header for distributed tracing.
    tracestate : str
        The tracestate header for distributed tracing.
    event_type : str
        The type of the event.
    """

    data: TEvent
    datacontenttype: str
    id: str
    pubsubname: str
    source: str
    specversion: str = "1.0"
    time: datetime
    topic: str
    traceparent: str | None = None
    tracestate: str | None = None
    type: str


@dataclass
class SubmissionCreatedEvent:
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
