# Podcasts API

This API is responsible for the podcast content generation. The API is triggered each
friday at 18:00 to generate the weekly podcast. We grab the content from last week and
use it to generate a podcast script. This script is then sent to ElevenLabs to generate
the podcast audio. The final podcast is then made available through buzzsprout.

## Getting started

1. Synchronize the python dependencies for the API.

   ```bash
   uv sync
   ```

2. Run the application by executing the following command:

   ```bash
   uv run agenttalks-podcasts
   ```

## Development

### Technology stack

- [FastAPI](https://fastapi.tiangolo.com)
- [Dapr](https://dapr.io)
- [Typer](https://typer.tiangolo.com)
- [Uvicorn](https://www.uvicorn.org)
- [OpenTelemetry](https://opentelemetry.io)

### Testing

We use [pytest](https://docs.pytest.org/en/stable/) to run unit-tests on the software.
Please use the following command to run the tests:

```bash
uv run pytest .
```
