"""Summarization activity for the reader workflow."""

from dataclasses import dataclass

from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.reader.workflow.runtime import workflow_runtime as wfr


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
    _ctx: WorkflowActivityContext, _input: SummarizeContentActivityInput
) -> SummarizeContentActivityResult:
    """Summarize the content of the submission.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context.
    input : int
        The input for the activity.
    """
    return SummarizeContentActivityResult(summary="This is a summary of the content.")
