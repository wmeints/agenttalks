import pytest

from agenttalks.content.models import ContentSubmission


def test_valid_status():
    valid_statuses = ["pending", "summarizing", "ready", "processed"]

    for status in valid_statuses:
        submission = ContentSubmission(
            id="test",
            url="http://localhost:3000",
            status=status,
            created_at="2023-10-01T00:00:00Z",
            updated_at="2023-10-01T00:00:00Z",
        )

        assert submission.status == status


def test_invalid_status():
    invalid_statuses = ["invalid", "unknown", "error"]

    for status in invalid_statuses:
        with pytest.raises(ValueError):  # noqa: PT011
            ContentSubmission(
                id="test",
                url="http://localhost:3000",
                status=status,
                created_at="2023-10-01T00:00:00Z",
                updated_at="2023-10-01T00:00:00Z",
            )
