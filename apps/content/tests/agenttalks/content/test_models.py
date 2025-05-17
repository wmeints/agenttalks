from datetime import UTC, datetime

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


def test_is_status_update_valid():
    # Test data: (current_status, new_status, expected_result)
    test_cases = [
        # Valid transitions
        ("pending", "summarizing", True),
        ("summarizing", "ready", True),
        ("ready", "processed", True),
        # Invalid transitions
        ("pending", "ready", False),
        ("pending", "processed", False),
        ("summarizing", "pending", False),
        ("summarizing", "processed", False),
        ("ready", "pending", False),
        ("ready", "summarizing", False),
        ("processed", "pending", False),
        ("processed", "summarizing", False),
        ("processed", "ready", False),
        # Invalid statuses
        ("pending", "invalid", False),
        ("summarizing", "invalid", False),
        ("ready", "invalid", False),
    ]

    for current_status, new_status, expected in test_cases:
        submission = ContentSubmission(
            id="test",
            url="http://localhost:3000",
            status=current_status,
            created_at=datetime.now(UTC),
            updated_at=datetime.now(UTC),
        )

        assert submission.is_status_update_valid(new_status) == expected, (
            f"Expected {expected} for transition from {current_status} to {new_status}"
        )
