---
applyTo: reader-api/**
---

# Copilot instructions for reader-api

This project uses Java with Quarkus to implement an API that summarizes submitted content
for inclusion into a weekly podcast episode. This API integrates with RabbitMQ using
reactive messaging to receive and publish events. We use an LLM to summarize the content
through Langchain4J.

## Writing tests

- Mark tests with `@QuarkusTest`.
- Use `@InjectMock` to inject mocked dependencies with mockito
- Mark test methods with `@Transactional` when accessing data in the database.
- Use arrange, act, assert style testing.

## Building and testing changes

Use `mvn verify` to build and test changes to the code.
