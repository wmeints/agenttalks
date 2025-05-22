"""Commandline interface for the reader app."""

from typing import Annotated

import uvicorn
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from typer import Option, Typer

from agenttalks.podcasts.server import app as server_app
from agenttalks.podcasts.telemetry import configure_tracing
from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr

app = Typer(name="agenttalks-podcasts")


@app.command()
def run_server(
    host: Annotated[str, Option(help="The host to bind to")] = "127.0.0.1",
    port: Annotated[int, Option(help="The port to bind to")] = 3002,
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

    wfr.start()
    uvicorn.run(server_app, host=host, port=port)
    wfr.shutdown()
