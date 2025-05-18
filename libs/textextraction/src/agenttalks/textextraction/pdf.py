"""Text extraction for PDF files."""

import logging

import pymupdf
from pymupdf import FileDataError

logger = logging.getLogger(__name__)
EMPTY_PDF_MSG = "PDF content cannot be empty"
INVALID_PDF_MSG = "The provided content is not a valid PDF file"
PAGE_ERROR_MSG = "Error extracting text from page %d: %s"
INVALID_PDF_LOG = "Invalid PDF file: %s"
PROCESSING_ERROR_LOG = "Error processing PDF: %s"


def extract_pdf_content(file_content: bytes) -> str:
    """Extract text from a PDF file.

    Parameters
    ----------
    file_content : bytes
        The content of the PDF file.


    Returns
    -------
    str
        The extracted text.
        Returns an empty string if the PDF could not be processed.

    Raises
    ------
    ValueError
        If the input is not a valid PDF file or is empty.
    """
    if not file_content:
        raise ValueError(EMPTY_PDF_MSG)

    try:
        # Open the PDF document from bytes
        with pymupdf.open(stream=file_content) as pdf_document:
            text_parts = []

            # Extract text from each page
            for page_num in range(len(pdf_document)):
                try:
                    page = pdf_document.load_page(page_num)
                    page_text = page.get_text("text")
                    if page_text:
                        text_parts.append(page_text)
                except Exception as e:  # noqa: BLE001
                    logger.warning(PAGE_ERROR_MSG, page_num + 1, str(e))
                    continue

            return "\n\n".join(text_parts) if text_parts else ""

    except FileDataError as e:
        logger.error(INVALID_PDF_LOG, str(e))
        raise ValueError(INVALID_PDF_MSG) from e
    except Exception as e:  # noqa: BLE001
        logger.error(PROCESSING_ERROR_LOG, str(e))
        raise
