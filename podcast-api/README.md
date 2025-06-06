# Podcast API

The Podcast API is responsible for generating AI-driven podcast episodes from content submissions in the Newscast application. It orchestrates a multi-step pipeline that converts summarized content into published podcast episodes using Azure OpenAI and ElevenLabs TTS services.

## Technical Requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/) (for database and message broker)
- [Maven](https://maven.apache.org/) (or use the included Maven wrapper)

Optional:
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling) for enhanced development experience

## Running the API in Development Mode

You can run the Podcast API in development mode using either the Quarkus CLI or Maven:

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
- Start with PostgreSQL and RabbitMQ dev services via Testcontainers
- Provide the Quarkus Dev UI at `http://localhost:8082/q/dev/`
- Enable observability features with LGTM stack (Grafana, Loki, Tempo, Prometheus)

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

The Podcast API follows a clean architecture approach with the following structure:

```
src/main/java/nl/fizzylogic/newscast/podcast/
├── clients/           # External service integrations
├── model/             # Domain models and entities  
├── processing/        # Pipeline steps for podcast generation
└── service/           # Core business logic and services

src/main/resources/
├── application.properties  # Configuration settings
└── prompts/               # AI prompt templates

src/test/java/         # Unit and integration tests
```

### Functional Areas

- **Clients** (`clients/`): External service integrations including Content API GraphQL client and ElevenLabs TTS client
- **Models** (`model/`): Domain models for podcast data structures like `PodcastEpisodeData`, `PodcastHost`, and `PodcastScript` 
- **Processing** (`processing/`): Pipeline steps that orchestrate the podcast generation workflow:
  - Content submission locking/unlocking
  - Podcast script generation using Azure OpenAI
  - Audio generation using ElevenLabs TTS
  - Audio mixing and post-processing
  - Publication to podcast platforms
- **Services** (`service/`): Core business logic including the scheduled `PodcastGenerationProcess` and AI-powered `PodcastScriptGenerator`

## API Endpoints

When running in development mode, you can access:

- **Quarkus Dev UI**: `http://localhost:8082/q/dev/`
- **Health Check**: `http://localhost:8082/q/health`
- **Metrics**: `http://localhost:8082/q/metrics`

The API runs on port **8082** to avoid conflicts with other services in the Newscast application.