"""Text extraction logic used by the agenttalks application."""

from agenttalks.textextraction.docx import extract_docx_content
from agenttalks.textextraction.html import extract_html_text
from agenttalks.textextraction.pdf import extract_pdf_content

DOCX_MIME_TYPE = (
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
)

PDF_MIME_TYPE = "application/pdf"
HTML_MIME_TYPE = "text/html"
TEXT_MIME_TYPE = "text/plain"


class UnsupportedMimeTypeError(Exception):
    """Custom exception for unsupported MIME types."""

    pass


def extract_text(mimetype: str, file_content: bytes, encoding: str) -> str:
    """Extract text from a document.

    Parameters
    ----------
    mimetype : str
        The MIME type of the document.
    file_content : bytes
        The content of the document.
    encoding : str
        The encoding of the document.

    Returns
    -------
    str
        The extracted text.
    """
    if mimetype == HTML_MIME_TYPE:
        return extract_html_text(file_content, encoding)

    if mimetype == TEXT_MIME_TYPE:
        return file_content.decode(encoding)

    if mimetype == PDF_MIME_TYPE:
        return extract_pdf_content(file_content)

    if mimetype == DOCX_MIME_TYPE:
        return extract_docx_content(file_content)

    exc_message = f"Unsupported MIME type: {mimetype}"
    raise UnsupportedMimeTypeError(exc_message)
