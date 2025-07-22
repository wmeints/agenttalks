# Agenttalks application

[![Continuous integration](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml/badge.svg)](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml)

Welcome to the agenttalks application! This is the AI-driven weekly podcast generator
developed by [joopsnijder](https://github.com/joopsnijder) and [wmeints](https://github.com/wmeints)!

The purpose of the agenttalks application is to help us gain insights into what's happening in
AI without having to read all the content that's available to us. The application will summarize
content for us and produce a weekly podcast for us to listen to, so we get a quick overview of
about 6 minutes rather than reading for multiple hours. We'll provide sources with
the podcast to access the complete information if we want to learn more
about a particular article.

## System requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/)
- [VSCode](https://code.visualstudio.com)
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling)

## Getting started

Before running the application, make sure you have the following environment variables in the `.env` file:

| Variable Name                                    | Description                                           |
| ------------------------------------------------ | ----------------------------------------------------- |
| QUARKUS_LANGCHAIN4J_AZURE_OPENAI_RESOURCE_NAME   | The resource name for your Azure OpenAI resource      |
| QUARKUS_LANGCHAIN4J_AZURE_OPENAI_API_KEY         | The API key for the Azure OpenAI resource             |
| QUARKUS_LANGCHAIN4J_AZURE_OPENAI_DEPLOYMENT_NAME | The name of the chat completion model you wish to use |
| AGENTTALKS_BUZZSPROUT_PODCAST_ID                 | The Podcast ID used in buzzsprout                     |
| AGENTTALKS_BUZZSPROUT_API_KEY                    | The API Key for buzzsprout                            |
| AGENTTALKS_ELEVENLABS_API_KEY                    | The API Key for ElevenLabs                            |
| AGENTTALKS_LOCATIONS_AUDIO_FILES                 | The directory where generated audio should be stored  |
| AGENTTALKS_LOCATIONS_FFMPEG                      | The full path where the ffmpeg binary is located      |

You can run the application locally with the Quarkus CLI using `quarkus dev`.

## Documentation

- [Product documentation](docs/product/README.md)
- [Architecture documentation](docs/architecture/README.md)
