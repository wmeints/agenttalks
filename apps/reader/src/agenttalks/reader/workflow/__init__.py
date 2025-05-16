"""Workflow implementation for the reader API."""

import logging

from dapr.ext.workflow import DaprWorkflowContext
from pydantic import BaseModel

from agenttalks.reader.workflow.activities import (
    DownloadContentActivityInput,
    UpdateStatusActivityInput,
    download_content_activity,
    store_summary_activity,
    summarize_content_activity,
    update_status_activity,
)
from agenttalks.reader.workflow.runtime import workflow_runtime as wfr


class ReplaySafeLogger:
    """A logger that is safe to use in the workflow code."""

    def __init__(self, ctx, base_logger=None):
        self.ctx = ctx
        self.base_logger = base_logger or logging.getLogger(__name__)

    def _should_log(self):
        return not self.ctx.is_replaying

    def info(self, msg, *args, **kwargs):
        if self._should_log():
            self.base_logger.info(msg, *args, **kwargs)

    def error(self, msg, *args, **kwargs):
        if self._should_log():
            self.base_logger.error(msg, *args, **kwargs)


class SummarizeWorkflowInput(BaseModel):
    """Input for the summarization workflow.

    Parameters
    ----------
    id : str
        The ID of the submission.
    url : str
        The URL of the content to summarize.
    instructions : str
        The instructions for the summarization.
    """

    id: str
    url: str
    instructions: str


@wfr.workflow(name="summarize_submission")
def summarize_submission_workflow(ctx: DaprWorkflowContext, input_data: any) -> any:
    """Summarize the content of a submission.

    This workflow downloads the content from the original website.
    Cleans up the received content and then summarizes using an agent by extracting
    the key talking points

    Parameters
    ----------
    ctx : DaprWorkflowContext
        The Dapr workflow context.
    input_data : SummarizeWorkflowInput
        The input data for the workflow.
    """
    wf_logger = ReplaySafeLogger(ctx)
    wf_logger.info("Starting workflow")

    yield ctx.call_activity(
        update_status_activity,
        input=UpdateStatusActivityInput(
            content_id=input_data["id"], status="summarizing"
        ),
    )

    yield ctx.call_activity(
        download_content_activity,
        input=DownloadContentActivityInput(
            url=input_data["url"], content_id=input_data["id"]
        ),
    )

    wf_logger.info("Downloaded the content")

    yield ctx.call_activity(summarize_content_activity, input=1)
    wf_logger.info("Summarized the content")

    yield ctx.call_activity(store_summary_activity, input=1)
    wf_logger.info("Stored the created summary")

    return {}
