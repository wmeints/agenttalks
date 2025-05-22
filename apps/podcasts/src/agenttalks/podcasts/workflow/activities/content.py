"""Content related activities for the podcast workflow."""

import logging
from dataclasses import dataclass
from datetime import datetime

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class PodcastSummary:
    """
    Data class for podcast summary.

    Attributes
    ----------
    title : str
        The title of the podcast episode
    url : str
        The URL of the podcast episode
    summary : str
        A brief summary of the podcast episode
    """

    title: str
    url: str
    summary: str


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
class CollectSummariesActivityResult:
    """Output data for the CollectSummariesActivity."""

    summaries: list[PodcastSummary]


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
    return CollectSummariesActivityResult()


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
