"""Download content activity."""

from dataclasses import dataclass
from typing import Tuple

import requests
from dapr.ext.workflow import WorkflowActivityContext
from requests.exceptions import RequestException

from agenttalks.reader.workflow.runtime import workflow_runtime as wfr
from agenttalks.textextraction import extract_text


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


def _get_content_type(response: requests.Response) -> Tuple[str, str]:
    """Extract content type and encoding from response.

    Args:
        response: The HTTP response object.


    Returns
    -------
    Tuple[str, str]
        A tuple of (content_type, encoding)
    """
    content_type = response.headers.get("content-type", "").split(";")[0].lower()
    encoding = response.encoding or "utf-8"
    return content_type, encoding


@wfr.activity(name="download_content")
def download_content_activity(
    _ctx: WorkflowActivityContext, input_data: DownloadContentActivityInput
) -> DownloadContentActivityResult:
    """Download the content of the submission.

    Parameters
    ----------
    _ctx : WorkflowActivityContext
        The workflow activity context.
    input_data : DownloadContentActivityInput
        The input data for the activity.

    Returns
    -------
    DownloadContentActivityResult
        The result containing the downloaded and processed content.

    Raises
    ------
    Exception
        If there's an error downloading or processing the content.
    """
    try:
        # Make the HTTP request
        response = requests.get(
            input_data.url,
            headers={
                "User-Agent": (
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 "
                    "Safari/537.36"
                )
            },
            timeout=30,
        )

        response.raise_for_status()

        try:
            content_type, encoding = _get_content_type(response)
            content = extract_text(content_type, response.content, encoding)
        except Exception:
            content = ""

        return DownloadContentActivityResult(content=content)
    except RequestException as e:
        error_msg = f"Failed to download content from {input_data.url}: {str(e)}"
        raise Exception(error_msg) from e
