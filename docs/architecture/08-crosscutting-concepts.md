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

## CI/CD workflow

Building, testing and deploying the application code is done through Github Actions.
We have a specific set up for the workflow. The workflow has the following jobs:

- Build and test
- Deploy to Azure Staging Environment
- Deploy to Azure Production Environment (Not yet implemented)

### Building and testing

The first step in the workflow is used to build and test the components. We run quarkus tests to verify that the general behavior of the application is correct. At the end of this job, we push the images to the container registry.

We tag images with `1.0.0-<hash>` to ensure we have images matching the github commit hash that was used to produce the images.

### Deploying to Azure staging environment

When the build and test job succeeds, we deploy the published container images to the Azure staging environment.

We manually verify that the installation succeeded on the staging environment.

### Deploying to Azure production environment

We use a manual approval for the staging environment. When the environment is approved, we deploy the application to the production environment.