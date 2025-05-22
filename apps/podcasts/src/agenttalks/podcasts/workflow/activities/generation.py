"""Podcast generation activity."""

import logging
from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class GeneratePodcastAudioActivityInput:
    """Input data for the GeneratePodcastAudioActivity."""

    pass


@dataclass
class GeneratePodcastAudioActivityResult:
    """Output data for the GeneratePodcastAudioActivity."""

    pass


@wfr.activity
def generate_podcast_audio_activity(
    _ctx: WorkflowActivityContext, _input_data: GeneratePodcastAudioActivityInput
) -> GeneratePodcastAudioActivityResult:
    """
    Generate podcast audio.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context
    input : GeneratePodcastAudioActivityInput
        Input data for the activity

    Returns
    -------
    GeneratePodcastAudioActivityResult
        Activity response with success/error information
    """
    return GeneratePodcastAudioActivityResult()
