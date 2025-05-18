"""Text extraction logic used by the agenttalks application."""

from agenttalks.textextraction.pdf import extract_pdf_content


def extract_text(mimetype: str, file_content: bytes) -> str:
    """Extract text from a document.

    Parameters
    ----------
    mimetype : str
        The MIME type of the document.
    file_content : bytes
        The content of the document.

    Returns
    -------
    str
        The extracted text.
    """
    if mimetype == "text/plain":
        return file_content.decode("utf-8")

    if mimetype == "application/pdf":
        return extract_pdf_content(file_content)

    exc_message = f"Unsupported MIME type: {mimetype}"
    raise ValueError(exc_message)
