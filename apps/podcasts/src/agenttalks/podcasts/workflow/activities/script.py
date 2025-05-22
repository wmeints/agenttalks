"""Script generation activity for podcasts."""

import logging
from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class GeneratePodcastScriptActivityInput:
    """Input data for the GeneratePodcastScriptActivity."""

    pass


@dataclass
class GeneratePodcastScriptActivityResult:
    """Output data for the GeneratePodcastScriptActivity."""

    pass


@wfr.activity
def generate_podcast_script_activity(
    _ctx: WorkflowActivityContext, _input_data: GeneratePodcastScriptActivityInput
) -> GeneratePodcastScriptActivityResult:
    """
    Generate podcast script.

    Args:
        ctx: The workflow activity context
        input: Input data for the activity

    Returns
    -------
        Activity response with success/error information
    """
    return GeneratePodcastScriptActivityResult()
