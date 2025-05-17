"""Defines the forms used by the HTTP interface to get input from the client."""

from pydantic import BaseModel


class UpdateStatusForm(BaseModel):
    """Input for the update status operation.

    Attributes
    ----------
    status : str
        The status to update the submission to.
    """

    status: str


class UpdateSummaryForm(BaseModel):
    """Input for the update summary operation.

    Attributes
    ----------
    summary : str
        The summary to update the submission to.
    """

    summary: str


class CreateSubmissionForm(BaseModel):
    """Input for the create submission operation.

    Attributes
    ----------
    url : str
        The URL of the submission.
    instructions : str | None
        The instructions for the submission.
    """

    url: str
    instructions: str | None
