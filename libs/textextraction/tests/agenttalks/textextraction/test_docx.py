from pathlib import Path

import pytest

from agenttalks.textextraction.docx import extract_docx_content


@pytest.fixture
def sample_docx_file() -> bytes:
    file_path = Path(__file__).parent / "test_files" / "sample-file.docx"
    return file_path.read_bytes()


def test_read_docx(sample_docx_file):
    content = extract_docx_content(sample_docx_file)
    assert content is not None
