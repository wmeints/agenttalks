"""The entrypoint for the HTTP server."""

from fastapi import FastAPI

from agenttalks.content.graph import router

app = FastAPI()
app.include_router(router)
