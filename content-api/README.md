# Content API

The Content API is responsible for managing content submissions for the Newscast application. It provides GraphQL endpoints for submitting content URLs, tracking submission status, and managing content lifecycle.

## Technical Requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/) (for database)
- [Maven](https://maven.apache.org/) (or use the included Maven wrapper)

Optional:
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling) for enhanced development experience

## Running the API in Development Mode

You can run the Content API in development mode using either the Quarkus CLI or Maven:

### Using Quarkus CLI (recommended)
```bash
quarkus dev
```

### Using Maven
```bash
./mvnw quarkus:dev
```

In development mode, the API will:
- Enable live coding with automatic reload on code changes
- Start with an in-memory H2 database for testing
- Provide a GraphQL UI at `http://localhost:8080/q/graphql-ui`
- Offer the Quarkus Dev UI at `http://localhost:8080/q/dev/`

## Running Tests

To run all tests including unit and integration tests:

```bash
mvn verify
```

To run only unit tests:

```bash
mvn test
```

## Directory Structure

The Content API follows a clean architecture approach with the following structure:

```
src/main/java/nl/fizzylogic/newscast/content/
├── eventbus/          # Event publishing and handling components
├── graph/             # GraphQL API layer and schema definitions
├── model/             # Domain models and entities
└── service/           # Business logic and service implementations

src/main/resources/
└── application.properties  # Configuration settings

src/test/java/         # Unit and integration tests
```

### Functional Areas

- **EventBus** (`eventbus/`): Handles publishing domain events when content submissions are created or updated
- **GraphQL Layer** (`graph/`): Exposes the public API for content submission and querying
- **Domain Models** (`model/`): Contains the core business entities like `ContentSubmission`
- **Services** (`service/`): Implements business logic for content lifecycle management

## API Endpoints

When running in development mode, you can access:

- **GraphQL API**: `http://localhost:8080/graphql`
- **GraphQL UI**: `http://localhost:8080/q/graphql-ui`
- **Health Check**: `http://localhost:8080/q/health`
- **Metrics**: `http://localhost:8080/q/metrics`