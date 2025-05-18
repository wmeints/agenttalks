"""Extract text from HTML content using BeautifulSoup."""

from bs4 import BeautifulSoup


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
            return element.get_text(separator="\n", strip=True)

    # Fallback to body if no main content found
    return soup.body.get_text(separator="\n", strip=True) if soup.body else ""
