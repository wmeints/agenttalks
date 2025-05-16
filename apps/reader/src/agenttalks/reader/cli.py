from typing import Annotated

from agenttalks.reader.server import app as server_app
from typer import Option, Typer

app = Typer(name="agenttalks-reader")


@app.command()
def run_server(
    host: Annotated[str, Option(help="The host to bind to")] = "127.0.0.1",
    port: Annotated[int, Option(help="The port to bind to")] = 3001,
) -> None:
    server_app.run(port, host)
