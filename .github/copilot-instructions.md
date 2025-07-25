# Agenttalks AI Podcast Generator - Copilot Instructions

## Project Overview

Agenttalks is an AI-driven weekly podcast generator that automatically summarizes AI 
news content and produces ~6-minute audio podcasts. The system collects content URLs, 
processes them with AI summarization (Azure OpenAI), and generates audio using 
ElevenLabs TTS for publication via Buzzsprout.

## Architecture Context

### Core Technologies
- **Framework**: Quarkus 3.24.3 + Java 21
- **Database**: PostgreSQL + Flyway migrations + Hibernate Panache
- **AI Integration**: Azure OpenAI (LangChain4J) + Semantic Kernel
- **Frontend**: React + TanStack Router + GraphQL + Clerk Auth
- **Infrastructure**: Azure Container Apps (Bicep templates in `infra/`)

## Feature Slice Architecture

When working on backend code, follow the feature slice pattern:

```
src/main/java/nl/infosupport/agenttalks/
├── content/     # Content management, GraphQL endpoints
├── podcast/     # Audio generation, Buzzsprout integration
├── reader/      # Content scraping & AI summarization
└── shared/      # Cross-cutting utilities, eventbus config
```

## Development Workflows

### Local Development

```bash
quarkus dev
```

### Build & Test

```bash
quarkus test
```

## AI Service Integration Patterns

### LangChain4J Service Pattern

```java
@RegisterAiService
public interface ContentSummarizer {
    @SystemMessage(fromResource = "prompts/summarizer/system-instructions.txt")
    @UserMessage(fromResource = "prompts/summarizer/user-instructions.txt")
    ContentSummary summarizeContent(String content);
}
```

### Fault Tolerance Pattern (Critical)

All AI service calls use comprehensive resilience:

```java
@Retry(maxRetries = 5, delay = 2000, jitter = 500)
@Timeout(value = 60000) // 60s for AI processing
@CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.6, delay = 10000)
ContentSummarizationData summarizeContent(ContentSummarizationData data) { ... }
```

## Event-Driven Processing

### Vert.x EventBus (Internal)

```java
@ConsumeEvent(value = "content.submission.created", blocking = true)
public void processContentSubmission(ContentSubmission submission) {
    // Linear processing: download → summarize → update
}
```

### Event Flow

1. **Content Submission** → `content.submission.created` → Reader processing
2. **Content Summarized** → `content.summary.completed` → Update content
3. **Podcast Generation** → Scheduled Fridays 6PM → Audio generation

## Database Patterns

### Panache Entity Pattern

```java
@Entity
public class ContentSubmission extends PanacheEntity {
    public static List<ContentSubmission> findProcessableSubmissions(LocalDate start, LocalDate end) {
        return find("processedAt IS NOT NULL AND createdAt BETWEEN ?1 AND ?2", start, end).list();
    }
}
```

### Migration Naming

Database migrations follow: `src/main/resources/db/migration/V{version}__{description}.sql`

## Configuration Conventions

### Environment-Specific Settings
- **Development**: Uses Clerk.dev for auth, local dev services
- **Production**: Azure-hosted services, secure key management
- **Testing**: H2 database, mocked external services

### Key External Services
- `quarkus.rest-client.elevenlabs.url=https://api.elevenlabs.io/`
- `quarkus.rest-client.buzzsprout.url=https://www.buzzsprout.com/api`
- Azure OpenAI configured via LangChain4J extensions

## Common Patterns & Anti-Patterns

### ✅ Do

- Use feature slicing for new backend functionality
- Apply fault tolerance to all external service calls
- Follow linear processing in event handlers (download → process → update)
- Use Panache for database operations
- Store AI prompts in `src/main/resources/prompts/`

### ❌ Avoid

- Creating new GraphQL client proxies (use direct service injection)
- Using RabbitMQ for new event handling (use Vert.x EventBus)
- Bypassing fault tolerance for AI/external service calls
- Breaking the linear content processing flow

## Infrastructure & Deployment

### Container Images

Registry: Azure Container Registry (configured in `quarkus.container-image.*`)

### Infrastructure as Code

- **Bicep templates**: `infra/` directory
- **Core resources**: `infra/core/` (database, container apps, OpenAI)
- **App resources**: `infra/app/` (service-specific configurations)

## Testing Strategy

### Key Test Scenarios

1. **Content processing flow**: URL submission → download → AI summarization → storage
2. **Podcast generation**: Weekly trigger → content aggregation → script generation → audio production
3. **Event bus messaging**: Verify Vert.x event flow between features
4. **External service resilience**: Fault tolerance behavior with AI/TTS services

Critical: Test external service integration with proper mocking in `%test` profile configurations.
