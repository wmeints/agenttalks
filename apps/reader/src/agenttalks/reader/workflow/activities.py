"""Workflow activities for the reader app."""

import json
from dataclasses import dataclass

from dapr.clients import DaprClient
from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.reader.workflow.runtime import workflow_runtime as wfr


@dataclass
class DownloadContentActivityInput:
    """Input for the download content activity.

    Attributes
    ----------
    url : str
        The URL of the content to download.
    instructions : str
        The instructions for the content to download.
    content_id : str
        The ID of the content to download.
    """

    url: str
    content_id: str


@dataclass
class DownloadContentActivityResult:
    """Result of the download content activity.

    Attributes
    ----------
    content : str
        The content downloaded.
    """

    content: str


@dataclass
class UpdateStatusActivityInput:
    """Input for the update status activity.

    Attributes
    ----------
    status : str
        The status to update.
    content_id : str
        The ID of the content to update.
    """

    status: str
    content_id: str


@dataclass
class UpdateSummaryActivityInput:
    """Input for the update summary activity.

    Attributes
    ----------
    summary : str
        The summary to update.
    content_id : str
        The ID of the content to update.
    """

    summary: str
    content_id: str


@wfr.activity(name="download_content")
def download_content_activity(
    _ctx: WorkflowActivityContext, input_data: DownloadContentActivityInput
) -> DownloadContentActivityResult:
    """Download the content of the submission."""
    return DownloadContentActivityResult(content="")


@wfr.activity(name="summarize_content")
def summarize_content_activity(ctx: WorkflowActivityContext, input: int) -> any:
    """Summarize the content of the submission."""
    pass


@wfr.activity(name="store_summary")
def store_summary_activity(
    _ctx: WorkflowActivityContext, input_data: UpdateSummaryActivityInput
) -> None:
    """Store the summary of the submission."""
    with DaprClient() as dapr_client:
        dapr_client.invoke_method(
            method_name="update_summary",
            data=json.dumps(
                {"content_id": input_data.content_id, "summary": input_data.summary}
            ),
            app_id="content",
        )


@wfr.activity(name="update_content_status")
def update_status_activity(
    _ctx: WorkflowActivityContext, input_data: UpdateStatusActivityInput
) -> None:
    """Update the status of the submission."""
    with DaprClient() as dapr_client:
        dapr_client.invoke_method(
            method_name="update_status",
            data=json.dumps(
                {"content_id": input_data.content_id, "status": input_data.status}
            ),
            app_id="content",
        )
