# Reader API for the agenttalks application

This directory contains the code for the reader API for the agenttalks application.
Please use this README to learn more about the structure of the API.

This application automatically summarizes and extracts knowledge from incoming articles.
It runs a small workflow downloading content and then calling an agent to summarize
the article and extract key talking points. The final sumamry is published to the
content API.

## Getting started

1. Synchronize the python dependencies for the API.

   ```bash
   uv sync
   ```

2. Run the application by executing the following command:

   ```bash
   uv run agenttalks-reader
   ```

## Development

### Technology stack

- [FastAPI](https://fastapi.tiangolo.com)
- [Dapr](https://dapr.io)
- [Typer](https://typer.tiangolo.com)
- [Uvicorn](https://www.uvicorn.org)

### Testing

We use [pytest](https://docs.pytest.org/en/stable/) to run unit-tests on the software.
Please use the following command to run the tests:

```bash
uv run pytest .
```
