"""Agent implementation for the reader API."""

from dapr.clients import DaprClient
from dapr_agents import ReActAgent
from dapr_agents.llm import OpenAIChatClient


def create_summarizer_agent() -> ReActAgent:
    """Create and return the agent."""
    with DaprClient() as dapr_client:
        llm_client = OpenAIChatClient(
            api_key=dapr_client.get_secret(
                store_name="app_secrets", key="languageModel:apiKey"
            ),
            azure_endpoint=dapr_client.get_secret(
                store_name="app_secrets", key="languageModel:endpoint"
            ),
            azure_deployment=dapr_client.get_secret(
                store_name="app_secrets", key="languageModel:chatDeployment"
            ),
            model="gpt-3.5-turbo",
            temperature=0.7,
            max_tokens=150,
        )

    return ReActAgent(
        name="summarizer",
        role=(
            "Provides insights into the content, including key points, summaries,"
            " and other relevant information."
        ),
        instructions=[
            (
                "Summarize the main points of the content. ",
                "Highlight any actionable insights. ",
                "Keep the summary concise and focused. ",
                "Return only the summary. No yapping. ",
            )
        ],
        llm=llm_client,
    )
