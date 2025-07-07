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

## Configuration

### Azure OpenAI Configuration (Reader API)

The Reader API uses LangChain4J with Azure OpenAI for content summarization. Configure the following properties in your `apps/reader-api/src/main/resources/application.properties`:

```properties
# Azure OpenAI Configuration
quarkus.langchain4j.azure-openai.api-key=${AZURE_OPENAI_API_KEY}
quarkus.langchain4j.azure-openai.endpoint=${AZURE_OPENAI_ENDPOINT}
quarkus.langchain4j.azure-openai.deployment-name=${AZURE_OPENAI_DEPLOYMENT_NAME}

# Chat Model Configuration
quarkus.langchain4j.azure-openai.chat-model.temperature=0.3
quarkus.langchain4j.azure-openai.chat-model.max-tokens=2048
quarkus.langchain4j.azure-openai.chat-model.timeout=30s
```

#### Environment Variables

Set the following environment variables:

```bash
export AZURE_OPENAI_API_KEY="your-azure-openai-api-key"
export AZURE_OPENAI_ENDPOINT="https://your-resource-name.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT_NAME="gpt-4o"
```

#### Configuration Properties

- `api-key`: Your Azure OpenAI API key
- `endpoint`: Your Azure OpenAI service endpoint URL
- `deployment-name`: The name of your deployed model (e.g., "gpt-4o-mini", "gpt-4o")
- `temperature`: Controls randomness in responses (0.0 = deterministic, 1.0 = very random)
- `max-tokens`: Maximum number of tokens in the response
- `timeout`: Request timeout duration

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
