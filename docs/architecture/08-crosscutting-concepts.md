# Cross-cutting concepts

## Observability

During development we use the [Observability dev
service](https://quarkus.io/guides/observability-devservices-lgtm). This service is
automatically started when you run one of the services in dev mode.

### Metrics

We use Prometheus with Micrometer to monitor metrics in the solution. Prometheus is
configured as a separate component during deployment.

### Tracing

We use OpenTelemetry with Tempo to record distributed traces in the application during
development. When deploying the application, we use Jaeger as the tracing tool.

### Logging

We use the built-in logging facilities in Quarkus to generate logs.
