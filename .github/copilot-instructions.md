# Solution structure

- apps/reader - Contains the Reader API, responsible for downloading content and summarizing it.
- apps/podcasts - Contains the Podcast production API, responsible for generating and publishing podcasts.
- apps/content - Contains the Content API, responsible for storing submissions, content summaries and podcast metadata.
- apps/portal - Contains the portal, used to manage the content and podcast metadata.

## Python practices

- Use ruff to format and lint the code.
- Use pytest for testing. 
- Use pytest-mock when mocks are needed.
- Follow best practices for writing tests, including:
  - Use fixtures to set up test data and dependencies.
  - Use parameterized tests to cover multiple scenarios.
  - Use descriptive names for test functions and assert statements.
- Follow pythonic conventions:
  - Use snake_case for variable and function names.
  - Use CamelCase for class names.
  - Use ALL_CAPS for constants.
  - Use spaces around operators and after commas.
  - Limit lines to 88 characters.
- Use type hints to improve code readability and maintainability.
- Use docstrings to document functions and classes.
- Use f-strings for string formatting.
- Use logging for debugging and error handling.

## Frontend practices

- Follow react and typescript best practices.
- Use functional components and hooks.
- Use SWR for data fetching.