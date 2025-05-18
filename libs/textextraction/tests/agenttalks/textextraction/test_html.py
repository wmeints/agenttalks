from pathlib import Path

import pytest

from agenttalks.textextraction.html import extract_html_text


@pytest.fixture
def sample_html_file() -> bytes:
    file_path = Path(__file__).parent / "test_files" / "sample-file.html"
    return file_path.read_bytes()


def test_read_html(sample_html_file):
    content = extract_html_text(sample_html_file, "utf-8")
    assert content is not None
