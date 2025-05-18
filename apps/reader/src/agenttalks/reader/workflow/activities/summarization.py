"""Summarization activity for the reader workflow."""

import asyncio
import logging
from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.reader.agent import create_summarizer_agent
from agenttalks.reader.workflow.runtime import workflow_runtime as wfr

logger = logging.getLogger(__name__)


@dataclass
class SummarizeContentActivityInput:
    """Input for the summarize content activity."""

    content: str


@dataclass
class SummarizeContentActivityResult:
    """Result for the summarize content activity."""

    summary: str


@wfr.activity(name="summarize_content")
def summarize_content_activity(
    _ctx: WorkflowActivityContext, input_data: SummarizeContentActivityInput
) -> SummarizeContentActivityResult:
    """Summarize the content of the submission.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context.
    input : int
        The input for the activity.
    """
    agent_instance = create_summarizer_agent()

    result = asyncio.get_event_loop().run_until_complete(
        agent_instance.run(input_data.content)
    )

    return SummarizeContentActivityResult(summary=result)
