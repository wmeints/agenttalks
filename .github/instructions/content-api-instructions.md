---
applyTo: content-api/**
---

# Copilot instructions for content-api

This project uses Java with Quarkus to implement a GraphQL based API for collecting
content for the weekly podcast. We use Postgres with Panache to work with the data
in the API.

## Writing tests

- Mark tests with `@QuarkusTest`.
- Use `@InjectMock` to inject mocked dependencies with mockito
- Mark test methods with `@Transactional` when accessing data in the database.
- Use arrange, act, assert style testing.

## Building and testing changes

Use `mvn verify` to build and test changes to the code.
