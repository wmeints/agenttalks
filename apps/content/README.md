# Content API for the agenttalks application

This directory contains the code for the content API for the agenttalks application.
Please use this README to learn more about the structure of the API.

## Getting started

1. Create a `.env` file with the following content:

   ```text
   APP_DATABASE_URL=<URL to the database>
   ```

   Make sure that it points to a MongoDB Instance, for example a MongoDB atlas cluster.

2. Synchronize the python dependencies for the API.

   ```bash
   uv sync
   ```

3. Run the application by executing the following command:

   ```bash
   uv run agenttalks-content
   ```

## Development

### Technology stack

- [FastAPI](https://fastapi.tiangolo.com)
- [PyMongo](https://pymongo.readthedocs.io/en/stable/)
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
