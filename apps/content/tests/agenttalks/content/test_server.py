"""Tests for the FastAPI server endpoints."""

from datetime import UTC, datetime
from unittest.mock import AsyncMock, MagicMock

import pytest
from fastapi import status
from fastapi.testclient import TestClient

from agenttalks.content.eventbus.client import create_event_publisher
from agenttalks.content.models import ContentSubmission
from agenttalks.content.persistence import create_submissions_repository
from agenttalks.content.server import app


@pytest.fixture
def mock_submissions_repository():
    """Create a mock SubmissionsRepository."""
    return AsyncMock()


@pytest.fixture
def mock_event_publisher():
    """Create a mock EventPublisher."""
    return MagicMock()


@pytest.fixture
def client(mock_submissions_repository, mock_event_publisher):
    """Create a test client with mocked dependencies."""

    def override_submissions_repo():
        return mock_submissions_repository

    def override_event_publisher():
        return mock_event_publisher

    app.dependency_overrides[create_submissions_repository] = override_submissions_repo
    app.dependency_overrides[create_event_publisher] = override_event_publisher

    with TestClient(app) as test_client:
        yield test_client

    # Clean up overrides after test
    app.dependency_overrides.clear()


@pytest.fixture
def sample_submission():
    """Create a sample submission for testing."""
    return ContentSubmission(
        id="test123",
        url="https://example.com",
        instructions="Test instructions",
        status="pending",
        created_at=datetime.now(UTC),
        updated_at=None,
    )


@pytest.mark.asyncio
async def test_set_status_valid_transition(
    client, mock_submissions_repository, sample_submission
):
    """Test setting a valid status transition."""
    # Setup
    submission_id = "test123"
    new_status = "summarizing"

    # Create a copy of the sample submission with updated status
    updated_submission = sample_submission.model_copy()
    updated_submission.status = new_status

    # Configure mocks
    mock_submissions_repository.find_submission_by_id.return_value = sample_submission

    mock_submissions_repository.update_submission_status.return_value = (
        updated_submission
    )

    # Act
    response = client.put(
        f"/submissions/{submission_id}/status",
        json={"status": new_status},
    )

    # Assert
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["status"] == new_status

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_status.assert_called_once_with(
        submission_id, new_status
    )


@pytest.mark.asyncio
async def test_set_status_invalid_transition(
    client, mock_submissions_repository, sample_submission
):
    """Test setting an invalid status transition."""
    # Setup
    submission_id = "test123"
    invalid_status = "processed"  # Can't go directly from pending to processed

    # Configure mock
    mock_submissions_repository.find_submission_by_id.return_value = sample_submission

    # Act
    response = client.put(
        f"/submissions/{submission_id}/status",
        json={"status": invalid_status},
    )

    # Assert
    assert response.status_code == status.HTTP_400_BAD_REQUEST
    assert "Invalid status update" in response.json()["detail"]

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_status.assert_not_called()


@pytest.mark.asyncio
async def test_set_status_nonexistent_submission(client, mock_submissions_repository):
    """Test setting status for a non-existent submission."""
    # Setup
    submission_id = "nonexistent"

    # Configure mock
    mock_submissions_repository.find_submission_by_id.return_value = None

    # Act
    response = client.put(
        f"/submissions/{submission_id}/status",
        json={"status": "summarizing"},
    )

    # Assert
    assert response.status_code == status.HTTP_404_NOT_FOUND
    assert "Submission not found" in response.json()["detail"]

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_status.assert_not_called()


@pytest.mark.asyncio
async def test_get_submission_success(
    client, mock_submissions_repository, sample_submission
):
    """Test successfully retrieving a submission by ID."""
    # Setup
    submission_id = "test123"

    # Configure mock
    mock_submissions_repository.find_submission_by_id.return_value = sample_submission

    # Act
    response = client.get(f"/submissions/{submission_id}")

    # Assert
    assert response.status_code == status.HTTP_200_OK
    response_data = response.json()
    assert response_data["id"] == sample_submission.id
    assert response_data["url"] == sample_submission.url
    assert response_data["status"] == sample_submission.status

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )


@pytest.mark.asyncio
async def test_get_submission_not_found(client, mock_submissions_repository):
    """Test retrieving a non-existent submission."""
    # Setup
    submission_id = "nonexistent"

    # Configure mock to return None (not found)
    mock_submissions_repository.find_submission_by_id.return_value = None

    # Act
    response = client.get(f"/submissions/{submission_id}")

    # Assert
    assert response.status_code == status.HTTP_404_NOT_FOUND
    assert response.json() == {"detail": "Submission not found"}

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_status.assert_not_called()


@pytest.mark.asyncio
async def test_update_submission_summary_success(
    client, mock_submissions_repository, sample_submission
):
    """Test successfully updating a submission's summary."""
    # Setup
    submission_id = "test123"
    new_summary = "This is an updated summary"

    # Create a copy of the sample submission with updated summary
    updated_submission = sample_submission.model_copy()
    updated_submission.summary = new_summary

    # Configure mocks
    mock_submissions_repository.find_submission_by_id.return_value = sample_submission

    mock_submissions_repository.update_submission_summary.return_value = (
        updated_submission
    )

    # Act
    response = client.put(
        f"/submissions/{submission_id}/summary",
        json={"summary": new_summary},
    )

    # Assert
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["summary"] == new_summary

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_summary.assert_called_once_with(
        submission_id, new_summary
    )


@pytest.mark.asyncio
async def test_update_submission_summary_not_found(client, mock_submissions_repository):
    """Test updating summary for a non-existent submission."""
    # Setup
    submission_id = "nonexistent"
    new_summary = "This summary won't be saved"

    # Configure mock to return None (not found)
    mock_submissions_repository.find_submission_by_id.return_value = None

    # Act
    response = client.put(
        f"/submissions/{submission_id}/summary",
        json={"summary": new_summary},
    )

    # Assert
    assert response.status_code == status.HTTP_404_NOT_FOUND
    assert response.json() == {"detail": "Submission not found"}

    mock_submissions_repository.find_submission_by_id.assert_called_once_with(
        submission_id
    )

    mock_submissions_repository.update_submission_summary.assert_not_called()


def test_get_submissions_success(client, mock_submissions_repository):
    """Test successfully retrieving paginated submissions."""
    # Arrange
    page = 1
    page_size = 20
    total_items = 35  # Will result in 2 pages

    # Create a list of submissions (20 items for page 1)
    submissions = [
        ContentSubmission(
            id=f"sub-{i}",
            url=f"https://example.com/{i}",
            title=f"Submission {i}",
            status="pending",
            created_at=datetime(2023, 1, 1, tzinfo=UTC),
        )
        for i in range(1, 21)  # 20 items
    ]

    # Mock the repository response
    mock_submissions_repository.find_submissions.return_value = (
        submissions,
        total_items,
    )

    # Act
    response = client.get(f"/submissions?page={page}")

    # Assert
    assert response.status_code == status.HTTP_200_OK
    data = response.json()

    # Check pagination metadata
    assert data["total"] == total_items
    assert data["page"] == page
    assert data["page_size"] == page_size
    assert data["total_pages"] == 2  # ceil(35/20) = 2

    # Check items
    assert len(data["items"]) == len(submissions)
    for i, item in enumerate(data["items"], 1):
        assert item["id"] == f"sub-{i}"
        assert item["url"] == f"https://example.com/{i}"

    # Verify repository was called with correct pagination parameters
    mock_submissions_repository.find_submissions.assert_called_once_with(
        skip=0,  # (page-1) * page_size = 0
        limit=page_size,
    )


def test_get_submissions_empty(client, mock_submissions_repository):
    """Test retrieving submissions when there are none."""
    # Arrange
    mock_submissions_repository.find_submissions.return_value = ([], 0)

    # Act
    response = client.get("/submissions")

    # Assert
    assert response.status_code == status.HTTP_200_OK
    data = response.json()

    assert data["total"] == 0
    assert data["items"] == []
    assert data["page"] == 1
    assert data["page_size"] == 20
    assert data["total_pages"] == 0

    mock_submissions_repository.find_submissions.assert_called_once_with(
        skip=0, limit=20
    )


def test_get_submissions_second_page(client, mock_submissions_repository):
    """Test retrieving the second page of submissions."""
    # Arrange
    page = 2
    page_size = 20
    total_items = 35

    # Create 15 items for page 2 (since total is 35)
    submissions = [
        ContentSubmission(
            id=f"sub-{i}",
            url=f"https://example.com/{i}",
            title=f"Submission {i}",
            status="pending",
            created_at=datetime(2023, 1, 1, tzinfo=UTC),
        )
        for i in range(21, 36)  # 15 items for page 2
    ]

    mock_submissions_repository.find_submissions.return_value = (
        submissions,
        total_items,
    )

    # Act
    response = client.get(f"/submissions?page={page}")

    # Assert
    assert response.status_code == status.HTTP_200_OK
    data = response.json()

    assert data["total"] == total_items
    assert data["page"] == page
    assert data["page_size"] == page_size
    assert data["total_pages"] == 2
    assert len(data["items"]) == 15  # 15 items on second page

    # Verify repository was called with correct pagination parameters
    mock_submissions_repository.find_submissions.assert_called_once_with(
        skip=20,  # (2-1) * 20 = 20
        limit=page_size,
    )
