"""Commandline interface for the reader app."""

from typing import Annotated

import uvicorn
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from opentelemetry.instrumentation.requests import RequestsInstrumentor
from typer import Option, Typer

from agenttalks.reader.server import app as server_app
from agenttalks.reader.telemetry import configure_tracing
from agenttalks.reader.workflow.runtime import workflow_runtime as wfr

app = Typer(name="agenttalks-reader")


@app.command()
def run_server(
    host: Annotated[str, Option(help="The host to bind to")] = "127.0.0.1",
    port: Annotated[int, Option(help="The port to bind to")] = 3001,
) -> None:
    """Run the HTTP server.

    Parameters
    ----------
    host: str
        The host to bind to
    port: str
        The port to bind to
    """
    configure_tracing()
    FastAPIInstrumentor(server_app)
    # RequestsInstrumentor().instrument()

    wfr.start()
    uvicorn.run(server_app, host=host, port=port)
    wfr.shutdown()
