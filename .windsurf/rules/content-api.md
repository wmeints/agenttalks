---
trigger: glob
globs: apps/content/**/*
---

- Use pytest to write unit-test for the project
- Use pytest-mock to create mocks when needed
- Use strawberry graphql with fastapi for the HTTP interface of the application
- Use pymongo to interact with the database
- Use dapr client to interact with other services
- Files related to the content API are stored in apps/content