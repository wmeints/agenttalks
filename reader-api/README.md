# Reader API

The Reader API is responsible for processing content submissions in the Newscast application. It handles downloading, parsing, and summarizing web content through an event-driven processing pipeline that integrates with AI services for content analysis.

## Technical Requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/) (for RabbitMQ message broker)
- [Maven](https://maven.apache.org/) (or use the included Maven wrapper)
- Access to Azure OpenAI or compatible LLM service for content summarization

Optional:
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling) for enhanced development experience

## Running the API in Development Mode

You can run the Reader API in development mode using either the Quarkus CLI or Maven:

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
- Connect to RabbitMQ message broker for event processing
- Provide the Quarkus Dev UI at `http://localhost:8080/q/dev/`
- Process content submission events from the content-api

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

The Reader API follows an event-driven processing architecture with the following structure:

```
src/main/java/nl/fizzylogic/newscast/reader/
├── clients/           # External service clients (Content API, AI services)
├── exceptions/        # Custom exception classes
├── model/             # Data models and DTOs for processing pipeline
├── processing/        # Event-driven processing steps
├── service/           # Business logic and service implementations
└── shared/            # Shared utilities and common components

src/main/resources/
├── application.properties  # Configuration settings
└── prompts/                # AI prompt templates for summarization

src/test/java/         # Unit and integration tests
```

### Functional Areas

- **Processing Pipeline** (`processing/`): Contains the main content processing steps:
  - `ContentDownloadStep`: Downloads and parses web content using Apache Tika
  - `ContentSummarizationStep`: Summarizes content using AI/LLM services  
  - `UpdateContentSubmissionStep`: Updates content submissions via GraphQL
- **External Clients** (`clients/`): Handles communication with external services
- **Domain Models** (`model/`): Contains data transfer objects for the processing pipeline
- **Services** (`service/`): Implements core business logic and AI integration