from unittest.mock import AsyncMock

import pytest

from agenttalks.content.graph import schema
from agenttalks.content.graph.context import ApplicationContext


@pytest.fixture
def mock_repository():
    mock_repo = AsyncMock()

    mock_repo.find_submissions.return_value = [
        {
            "_id": "1",
            "url": "http://example.com",
            "instructions": "Test instructions",
            "status": "pending",
            "created_at": "2023-10-01T00:00:00Z",
            "updated_at": "2023-10-01T00:00:00Z",
        }
    ]

    return mock_repo


@pytest.fixture
def mock_app_context(mock_repository):
    return ApplicationContext(submissions_repository=mock_repository)


@pytest.mark.asyncio
async def test_submissions(mock_app_context):
    result = await schema.execute(
        "{ submissions { url instructions status } }",
        context_value=mock_app_context,
    )

    assert len(result.data["submissions"]) == 1
    assert result.data["submissions"][0]["url"] == "http://example.com"
    assert result.data["submissions"][0]["instructions"] == "Test instructions"
    assert result.data["submissions"][0]["status"] == "pending"


@pytest.mark.asyncio
async def test_set_status(mock_app_context):
    result = await schema.execute(
        """
        mutation {
            setStatus(input: { id: "1", status: "ready" })
        }
        """,
        context_value=mock_app_context,
    )

    assert result.errors is None


@pytest.mark.asyncio
async def test_submit_content(mock_app_context):
    result = await schema.execute(
        """
        mutation {
            submitContent(input: { 
                url: "http://example.com", 
                instructions: "Test instructions" 
            })
        }
        """,
        context_value=mock_app_context,
    )

    assert result.errors is None
