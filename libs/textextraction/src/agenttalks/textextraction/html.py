"""Extract text from HTML content using BeautifulSoup."""

from bs4 import BeautifulSoup


def _cleanup_text(text: str) -> str:
    """Clean up the extracted text by removing unwanted characters.

    This method removes non-ASCII characters from the unicode string.

    Parameters
    ----------
    text : str
        The text to clean up.

    Returns
    -------
    str
        Cleaned-up text.
    """
    return "".join(c for c in text if ord(c) < 128).strip()


def extract_html_text(content: bytes, encoding: str) -> str:
    """Extract main content from HTML using BeautifulSoup.

    Parameters
    ----------
    content : bytes
        The HTML content to extract text from.
    encoding : str
        The encoding of the HTML content.

    Returns
    -------
    str
        Extracted text content.
    """
    soup = BeautifulSoup(content.decode(encoding), "html.parser")

    # Try to find the main content using common HTML5 semantic elements
    for tag in ["main", "article", "div.content", "div.main", "div.article"]:
        element = soup.select_one(tag)
        if element:
            extracted_text = element.get_text(separator="\n", strip=True)
            return _cleanup_text(extracted_text)

    # Fallback to body if no main content found
    extracted_text = soup.body.get_text(separator="\n", strip=True) if soup.body else ""
    return _cleanup_text(extracted_text)
