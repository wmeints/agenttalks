"""Agent implementation for the reader API."""

from dapr.clients import DaprClient
from dapr_agents import ReActAgent
from dapr_agents.llm import OpenAIChatClient


def create_summarizer_agent() -> ReActAgent:
    """Create and return the agent."""
    with DaprClient() as dapr_client:
        api_key = dapr_client.get_secret("app_secrets", "languageModel:apiKey").secret[
            "languageModel:apiKey"
        ]

        endpoint = dapr_client.get_secret(
            "app_secrets", "languageModel:endpoint"
        ).secret["languageModel:endpoint"]

        chat_deployment = dapr_client.get_secret(
            "app_secrets", "languageModel:chatDeployment"
        ).secret["languageModel:chatDeployment"]

        llm_client = OpenAIChatClient(
            api_key=api_key,
            azure_endpoint=endpoint,
            azure_deployment=chat_deployment,
            model="gpt-3.5-turbo",
            temperature=0.7,
            max_tokens=150,
        )

    agent_prompt: str = (
        "You are a summarizer agent. Your task is to summarize the content "
        "provided to you. You will receive a text input, and your job is to "
        "provide a concise summary of the main points and any actionable insights."
    )

    return ReActAgent(
        name="summarizer",
        role=(
            "Provides insights into the content, including key points, summaries,"
            " and other relevant information."
        ),
        instructions=[agent_prompt],
        llm=llm_client,
    )
