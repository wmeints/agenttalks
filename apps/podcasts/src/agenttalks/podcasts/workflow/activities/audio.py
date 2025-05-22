"""Audio related activities for the podcast workflow."""

import logging
from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class ConcatenateAudioActivityInput:
    """Input data for the ConcatenateAudioActivity.

    Attributes
    ----------
    podcast_audio : str
        The path to the podcast audio file
    """

    podcast_audio: str


@dataclass
class ConcatenateAudioActivityResult:
    """
    Output data for the ConcatenateAudioActivity.

    Attributes
    ----------
    output_path : str
        The path to the concatenated audio file
    """

    output_path: str


@wfr.activity
def concatenate_audio_activity(
    _ctx: WorkflowActivityContext, _input_data: ConcatenateAudioActivityInput
) -> ConcatenateAudioActivityResult:
    """
    Concatenate audio fragments.

    Downloads the intro, outro, and podcast audio files from Azure Blob Storage,
    then concatenates them into a single podcast audio file. The final result
    is then uploaded back to Azure Blob Storage.

    The intro and outro files are configurable in the Dapr config store. Please refer
    to the documentation for more information on how to set up the config store values.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context
    input_data : ConcatenateAudioActivityInput
        Input data for the activity

    Returns
    -------
    ConcatenateAudioActivityOutput
        Activity response with success/error information
    """
    return ConcatenateAudioActivityResult(output_path="")
