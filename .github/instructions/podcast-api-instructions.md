---
applyTo: podcast-api/**
---

# Copilot instructions for podcast-api

This project uses Java with Quarkus to implement the podcast generation process in the
newscast application. The API integrates with RabbitMQ using reactive messaging
and uses Langchain4J to interact with an LLM to generate a podcast script. We use
ElevenLabs as the text to speech for generating podcast audio.

## Writing tests

- Mark tests with `@QuarkusTest`.
- Use `@InjectMock` to inject mocked dependencies with mockito
- Mark test methods with `@Transactional` when accessing data in the database.
- Use arrange, act, assert style testing.

## Building and testing changes

Use `mvn verify` to build and test changes to the code.
