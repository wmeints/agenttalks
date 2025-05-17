"""Workflow activities for the reader app.

The activities interact with other services and components to implement steps in the
workflow. The workflow itself never interacts with external dependencies. It only
builds a graph for the workflow runtime to execute.
"""

import json
from dataclasses import dataclass

from dapr.clients import DaprClient
from dapr.ext.workflow import WorkflowActivityContext

from agenttalks.reader.workflow.activities.download import (
    DownloadContentActivityInput as DownloadContentActivityInput,
)
from agenttalks.reader.workflow.activities.download import (
    DownloadContentActivityResult as DownloadContentActivityResult,
)
from agenttalks.reader.workflow.activities.download import (
    download_content_activity as download_content_activity,
)
from agenttalks.reader.workflow.runtime import workflow_runtime as wfr


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


@wfr.activity(name="summarize_content")
def summarize_content_activity(ctx: WorkflowActivityContext, input: int) -> any:
    """Summarize the content of the submission.

    Parameters
    ----------
    ctx : WorkflowActivityContext
        The workflow activity context.
    input : int
        The input for the activity.
    """
    pass


@wfr.activity(name="store_summary")
def store_summary_activity(
    _ctx: WorkflowActivityContext, input_data: UpdateSummaryActivityInput
) -> None:
    """Store the summary of the submission.

    Parameters
    ----------
    _ctx : WorkflowActivityContext
        The workflow activity context.
    input_data : UpdateSummaryActivityInput
        The input data for the activity.
    """
    with DaprClient() as dapr_client:
        dapr_client.invoke_method(
            http_verb="put",
            method_name=f"submissions/{input_data.content_id}/summary",
            data=json.dumps({"summary": input_data.summary}),
            app_id="content",
        )


@wfr.activity(name="update_content_status")
def update_status_activity(
    _ctx: WorkflowActivityContext, input_data: UpdateStatusActivityInput
) -> None:
    """Update the status of the submission.

    Parameters
    ----------
    _ctx : WorkflowActivityContext
        The workflow activity context.
    input_data : UpdateStatusActivityInput
        The input data for the activity.
    """
    with DaprClient() as dapr_client:
        dapr_client.invoke_method(
            http_verb="put",
            method_name=f"submissions/{input_data.content_id}/status",
            data=json.dumps({"status": input_data.status}),
            app_id="content",
        )
