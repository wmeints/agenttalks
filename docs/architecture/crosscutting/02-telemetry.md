# Telemetry

We use [OpenTelemetry](https://opentelemetry.io/) to collect telemetry data from the application.
Most of the instrumentation is done through the Dapr sidecar. But to make sure we flow traces
correctly through the application we use additional instrumentation in the API.

## FastAPI Integration package

We use the FastAPI integration package to instrument the various API endpoints.
You can find the package [here](https://pypi.org/project/opentelemetry-instrumentation-fastapi/).

To instrument an API, use the following code:

```python
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor

FastAPIInstrumentor.instrument_app(app)
```

Instrumentation must happen in the `cli` module as we have a fully initialized app
instance there.

## Telemetry collection

All telemetry data is collected in zipkin. For local development you can navigate
to http://localhost:9411 to view the traces.
