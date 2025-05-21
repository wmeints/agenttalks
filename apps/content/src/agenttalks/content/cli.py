"""Entrypoint for the application."""

from typing import Annotated

import uvicorn
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from typer import Option, Typer

from agenttalks.content import server
from agenttalks.content.telemetry import configure_tracing

app = Typer(name="agenttalks-content")


@app.command()
def run_server(
    host: Annotated[str, Option(help="The host to bind to")] = "127.0.0.1",
    port: Annotated[int, Option(help="The port to bind to")] = 3000,
) -> None:
    """Run the HTTP server.

    Parameters
    ----------
    host: str
        The host to bind to
    port: int
        The port to bind to
    """
    configure_tracing()
    FastAPIInstrumentor.instrument_app(server.app)

    uvicorn.run(server.app, host=host, port=port)
