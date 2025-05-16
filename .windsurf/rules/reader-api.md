---
trigger: glob
globs: apps/reader/**/*
---

- Use pytest to write unit-test for the project
- Use pytest-mock to create mocks when needed
- Use the dapr client to interact with other services
- Use the dapr fastapi extension to subscribe to events, etc.