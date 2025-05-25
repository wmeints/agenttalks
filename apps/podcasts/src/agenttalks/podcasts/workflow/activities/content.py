"""Content related activities for the podcast workflow."""

import logging
from dataclasses import dataclass
from datetime import datetime
from urllib.parse import quote

from dapr.clients import DaprClient
from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class CollectSummariesActivityInput:
    """Input data for the CollectSummariesActivity.

    Attributes
    ----------
    start_date : datetime
        The start date for the summaries
    end_date : datetime
        The end date for the summaries
    """

    start_date: datetime
    end_date: datetime


@dataclass
class SubmissionListItem:
    """Model for a submission in the list response.

    Attributes
    ----------
    id: str
        The ID of the submission
    url: str
        The URL of the submitted content
    summary: str
        The summary of the content, if available
    created_at: datetime
        The creation date of the submission
    """

    id: str
    url: str
    summary: str
    created_at: datetime

    @classmethod
    def from_dict(cls, data: dict) -> "SubmissionListItem":
        """Create a SubmissionListItem instance from a dictionary.

        Parameters
        ----------
        data : dict
            The data dictionary containing submission details

        Returns
        -------
        SubmissionListItem
            The SubmissionListItem instance created from the data

        Raises
        ------
        ValueError
            If the data does not contain the required fields or if the date format is
            incorrect
        """
        return cls(
            id=data["id"],
            url=data["url"],
            summary=data.get("summary"),
            created_at=datetime.fromisoformat(data["created_at"]),
        )


@dataclass
class CollectSummariesActivityResult:
    """Output data for the CollectSummariesActivity."""

    summaries: list[SubmissionListItem]


@dataclass
class SavePodcastMetadataActivityInput:
    """Input data for the SavePodcastMetadataActivity."""

    pass


@dataclass
class SavePodcastMetadataActivityResult:
    """Output data for the SavePodcastMetadataActivity."""

    pass


@wfr.activity
def collect_summaries_activity(
    _ctx: WorkflowActivityContext, _input_data: CollectSummariesActivityInput
) -> CollectSummariesActivityResult:
    """
    Collect summaries for the current week.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context
    input : CollectSummariesActivityInput
        Input data for the activity

    Returns
    -------
    CollectSummariesActivityResult
        Activity response with success/error information
    """
    with DaprClient() as dapr_client:
        start_date_str = quote(_input_data.start_date.isoformat())
        end_date_str = quote(_input_data.end_date.isoformat())

        response = dapr_client.invoke_method(
            "content",
            "submissions/by-date",
            http_verb="GET",
            http_querystring=[
                ("start_date", start_date_str),
                ("end_date", end_date_str),
            ],
        )

        collected_summaries = [
            SubmissionListItem.from_dict(item) for item in response.json()
        ]

        return CollectSummariesActivityResult(summaries=collected_summaries)


@wfr.activity
def save_podcast_metadata_activity(
    _ctx: WorkflowActivityContext, _input_data: SavePodcastMetadataActivityInput
) -> SavePodcastMetadataActivityResult:
    """
    Store podcast metadata.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context
    input_data : SavePodcastMetadataActivityInput
        Input data for the activity

    Returns
    -------
    SavePodcastMetadataActivityResult
        Activity response with success/error information
    """
    return SavePodcastMetadataActivityResult()
