# Cross-cutting concepts

## Observability

The application uses JBoss Logging as the primary logging framework, which is the
default logging implementation in Quarkus. This provides structured logging capabilities
across all application components.

### Logging Strategy

- **Framework**: JBoss Logging with SLF4J facade
- **Log Levels**: Configured per package/class for appropriate verbosity
- **Structured Logging**: JSON format for production environments to facilitate log
  aggregation
- **Local Development**: Human-readable console output for debugging

### Key Logging Areas

- **Content Processing**: Detailed logs for URL scraping, AI summarization, and content
  storage
- **Event Bus Operations**: Tracking of Vert.x event flows between features
- **External Service Calls**: Request/response logging for AI services (Azure OpenAI),
  TTS (ElevenLabs), and publishing (Buzzsprout)
- **Database Operations**: Hibernate/Panache query logging in development
- **Fault Tolerance**: Circuit breaker state changes and retry attempts

### Future Observability Enhancements

OpenTelemetry integration was temporarily removed due to configuration issues but should
be re-added in future iterations to provide:

- Distributed tracing across service boundaries
- Metrics collection for performance monitoring
- Integration with Azure Application Insights for comprehensive observability

## CI/CD workflow

Building, testing and deploying the application code is done through Github Actions.
We have a specific set up for the workflow. The workflow has the following jobs:

- Build and test
- Deploy to Azure Staging Environment
- Deploy to Azure Production Environment (Not yet implemented)

### Building and testing

The first step in the workflow is used to build and test the components. We run quarkus
tests to verify that the general behavior of the application is correct. At the end of
this job, we push the images to the container registry.

We tag images with `1.0.0-<hash>` to ensure we have images matching the github commit
hash that was used to produce the images.

### Deploying to Azure staging environment

When the build and test job succeeds, we deploy the published container images to the
Azure staging environment.

We manually verify that the installation succeeded on the staging environment.

### Deploying to Azure production environment

We use a manual approval for the staging environment. When the environment is approved,
we deploy the application to the production environment.
