"""Workflow implementation for the reader API."""

from dataclasses import dataclass
from datetime import timedelta

from dapr.ext.workflow import DaprWorkflowContext, RetryPolicy

from agenttalks.reader.workflow.activities import (
    DownloadContentActivityInput,
    UpdateStatusActivityInput,
    download_content_activity,
    store_summary_activity,
    summarize_content_activity,
    update_status_activity,
)
from agenttalks.reader.workflow.activities.content import UpdateSummaryActivityInput
from agenttalks.reader.workflow.activities.summarization import (
    SummarizeContentActivityInput,
)
from agenttalks.reader.workflow.logging import ReplaySafeLogger
from agenttalks.reader.workflow.runtime import workflow_runtime as wfr


@dataclass
class SummarizeSubmissionWorkflowInput:
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

    content_id: str
    url: str
    instructions: str


@dataclass
class SummarizeSubmissionWorkflowData:
    """Workflow related data for the summarization workflow.

    Attributes
    ----------
    content_id : str
        The ID of the submission.
    content : str
        The content to summarize.
    summary : str
        The summary of the content.
    """

    content_id: str
    url: str
    instructions: str
    content: str | None = None
    summary: str | None = None

    @classmethod
    def from_input(
        cls, input_data: SummarizeSubmissionWorkflowInput
    ) -> "SummarizeSubmissionWorkflowData":
        """Create a SummarizeSubmissionWorkflowData object from input data.

        Parameters
        ----------
        cls : type
            The class to create an instance of.
        input_data : SummarizeSubmissionWorkflowInput
            The input data for the workflow.

        Returns
        -------
        SummarizeSubmissionWorkflowData
            The created workflow data object.
        """
        return cls(
            content_id=input_data.content_id,
            url=input_data.url,
            instructions=input_data.instructions,
        )


@wfr.workflow(name="summarize_submission")
def summarize_submission_workflow(
    ctx: DaprWorkflowContext, input_data: SummarizeSubmissionWorkflowInput
) -> any:
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

    instance_data = SummarizeSubmissionWorkflowData.from_input(input_data)

    yield ctx.call_activity(
        update_status_activity,
        input=UpdateStatusActivityInput(
            content_id=input_data.content_id, status="summarizing"
        ),
        retry_policy=RetryPolicy(
            max_number_of_attempts=3, first_retry_interval=timedelta(seconds=1)
        ),
    )

    # Content download can fail because the external server is broken.
    # We retry max 10 times with a 30 second interval.

    download_result = yield ctx.call_activity(
        download_content_activity,
        input=DownloadContentActivityInput(
            url=input_data.url, content_id=input_data.content_id
        ),
        retry_policy=RetryPolicy(
            max_number_of_attempts=10, first_retry_interval=timedelta(seconds=30)
        ),
    )

    # Store the intermediate result in the workflow data
    # The workflow data is stored in the statestore, so we can request it later.
    instance_data.content = download_result.content

    wf_logger.info("Downloaded the content")

    # Summarization can fail because the LLM isn't available.
    # We retry max 3 times with a 30 second interval.

    summarization_result = yield ctx.call_activity(
        summarize_content_activity,
        input=SummarizeContentActivityInput(content=download_result.content),
        retry_policy=RetryPolicy(
            max_number_of_attempts=3, first_retry_interval=timedelta(seconds=30)
        ),
    )

    # Store the intermediate result in the workflow data
    # The workflow data is stored in the statestore, so we can request it later.
    instance_data.summary = summarization_result.summary

    wf_logger.info("Summarized the content")

    yield ctx.call_activity(
        store_summary_activity,
        input=UpdateSummaryActivityInput(
            summarization_result.summary, content_id=input_data.content_id
        ),
        retry_policy=RetryPolicy(
            max_number_of_attempts=3, first_retry_interval=timedelta(seconds=1)
        ),
    )

    wf_logger.info("Stored the created summary")

    return instance_data
