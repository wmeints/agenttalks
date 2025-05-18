from pathlib import Path

import pytest

from agenttalks.textextraction.pdf import extract_pdf_content


@pytest.fixture
def sample_pdf_file() -> bytes:
    file_path = Path(__file__).parent / "test_files" / "sample-file.pdf"
    return file_path.read_bytes()


def test_read_pdf(sample_pdf_file):
    content = extract_pdf_content(sample_pdf_file)
    assert content is not None
