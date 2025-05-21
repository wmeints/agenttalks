"""Telemetry configuration for the API."""

from typing import Dict

from opentelemetry import trace
from opentelemetry.exporter.zipkin.json import ZipkinExporter
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.trace.propagation.tracecontext import TraceContextTextMapPropagator

tracer_provider = None


def configure_tracing() -> None:
    """Get the tracer provider.

    The provider instance is memoized on module level to ensure we only have one tracer
    provider.
    """
    global tracer_provider

    if tracer_provider is None:
        tracer_provider = TracerProvider(resource=Resource({"service.name": "content"}))

        span_processor = BatchSpanProcessor(
            ZipkinExporter(endpoint="http://localhost:9411/api/v2/spans")
        )

        tracer_provider.add_span_processor(span_processor)
        trace.set_tracer_provider(tracer_provider)


def get_tracer(name: str) -> trace.Tracer:
    """
    Get a tracer.

    Parameters
    ----------
    name : str
        The name of the tracer.

    Returns
    -------
    trace.Tracer
        The tracer.
    """
    configure_tracing()
    return trace.get_tracer(name)


def get_tracing_headers() -> Dict[str, str]:
    """Create a trace injector for HTTP requests.

    Returns
    -------
    Dict[str, str]
        The tracecontext headers.
    """
    headers = {}
    TraceContextTextMapPropagator().inject(carrier=headers)
    return headers


def get_current_traceparent() -> str:
    """Get the current traceparent."""
    headers = {}
    TraceContextTextMapPropagator().inject(carrier=headers)
    return headers.get("traceparent")
