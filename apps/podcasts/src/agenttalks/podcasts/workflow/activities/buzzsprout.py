"""Buzzsprout activity for publishing podcast episodes."""

import logging
from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class PublishToBuzzsproutActivityInput:
    """
    Input data for the PublishToBuzzsproutActivity.

    Attributes
    ----------
    title : str
        The title of the podcast episode
    description : str
        The description of the podcast episode
    podcast_audio : str
        The path to the podcast audio file in azure blob storage
    """

    title: str
    description: str
    podcast_audio: str


@dataclass
class PublishToBuzzsproutActivityResult:
    """
    Output data for the PublishToBuzzsproutActivity.

    Attributes
    ----------
    buzzsprout_episode_id : str
        The ID of the episode in Buzzsprout
    buzzsprout_episode_url : str
        The URL of the episode in Buzzsprout
    buzzsprout_episode_number : int
        The episode number in Buzzsprout
    """

    buzzsprout_episode_id: str
    buzzsprout_episode_url: str
    buzzsprout_episode_number: int


@wfr.activity
def publish_to_buzzsprout_activity(
    _ctx: WorkflowActivityContext, _input_data: PublishToBuzzsproutActivityInput
) -> PublishToBuzzsproutActivityResult:
    """
    Publish to buzzsprout.

    Parameters
    ----------
    ctx: WorkflowActivityContext
        The workflow activity context
    input_data: PublishToBuzzsproutActivityInput
        Input data for the activity

    Returns
    -------
    PublishToBuzzsproutActivityResult
        Activity response with success/error information
    """
    return PublishToBuzzsproutActivityResult(
        buzzsprout_episode_id="", buzzsprout_episode_url="", buzzsprout_episode_number=0
    )
