"""The entrypoint for the HTTP server."""

from agenttalks.content.telemetry import get_tracer
from dapr.ext.fastapi import DaprApp
from fastapi import FastAPI

app = FastAPI()
dapr_app = DaprApp(app)
tracer = get_tracer(__name__)
