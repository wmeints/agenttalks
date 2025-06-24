# Reader API

The Reader API is responsible for downloading and summarizing content for the weekly
podcast. It uses Azure OpenAI to summarize the downloaded content.

## Technical Requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/)
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
- Offer the Quarkus Dev UI at `http://localhost:8081/q/dev/`

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
src/main/java/nl/fizzylogic/newscast/reader/
├── clients/           # Clients used to connect to other services
├── exceptions/        # Exceptions used in the application
├── processing/        # Components used in the content processing pipeline
├── model/             # Domain models and entities
└── service/           # Business logic and service implementations

src/main/resources/
└── application.properties  # Configuration settings

src/test/java/         # Unit and integration tests
```
