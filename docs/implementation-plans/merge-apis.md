# Backend APIs Merge Implementation Plan

## Task Overview

Merge all backend APIs (`content-api`, `podcast-api`, `reader-api`) into a single
`content-api` application using feature slicing architecture. Replace RabbitMQ with
Vert.x eventbus for internal communication and eliminate GraphQL client proxies in favor
of direct feature calls.

## Critical Context

### Current Architecture

- **content-api**: Main GraphQL API with content submission and podcast episode management
- **podcast-api**: Podcast generation using Temporal workflows, AI script generation, and audio processing
- **reader-api**: Content scraping, downloading, and AI summarization

### Key Dependencies and Technologies

- **Framework**: Quarkus 3.24.3 with Java 21
- **Database**: PostgreSQL with Flyway migrations and Hibernate Panache
- **Messaging**: Currently RabbitMQ → Vert.x EventBus
- **AI Services**: Azure OpenAI via LangChain4J and Semantic Kernel
- **Cloud Storage**: Azure Storage Blob
- **Publishing**: Buzzsprout API integration
- **Architecture**: Clean architecture with feature slicing

### Documentation References

- **Quarkus Vert.x Event Bus**: https://quarkus.io/guides/reactive-event-bus
- **Quarkus Vert.x Integration**: https://quarkus.io/guides/vertx
- **Feature Slicing Pattern**: Package-by-feature organization in Quarkus applications
- **Quarkus CDI Integration**: https://quarkus.io/guides/cdi-integration

## Database Schema Analysis

### Current Schemas

#### Content API Schema (V1-V3)

```sql
-- V1__CreateInitialDatabaseStructure.sql
create table content_submission (
    id bigint generated always as identity primary key,
    url varchar(1000) not null,
    title varchar(500) null,
    summary text null,
    date_created timestamp not null,
    date_modified timestamp null,
    submission_status varchar(50) not null
);

create table podcast_episode(
    id bigint generated always as identity primary key,
    title varchar(500) not null,
    audio_file varchar(1000) not null,
    script text null,
    date_created timestamp not null
);

-- V2__AddPodcastEpisodeMetadata.sql
alter table podcast_episode 
add column episode_number int not null default 1;
add column show_notes text not null default '';
add column description text not null default '';

-- V3__AddEpisodeNumberSequence.sql
CREATE SEQUENCE podcast_episode_number_seq START 1;
```

#### Podcast API Schema
```sql
-- V1__CreatePodcastHost.sql
CREATE TABLE podcast_host (
    id bigint generated always as identity,
    name varchar(250) not null,
    style_instructions text not null,
    language_patterns text not null,
    voice_id varchar(250) not null,
    index int not null,
    PRIMARY KEY (id) 
);

INSERT INTO podcast_host(index, name, style_instructions, language_patterns, voice_id) values 
(1, 'Joop Snijder', '', '', 'Vm0vfS4svZOhBRMa4ap3'),
(2, 'Willem Meints', '', '', 'XeK7XhqnGf6zErDLDutX');
```

#### Reader API Schema
- No dedicated database tables (uses GraphQL client to communicate with content-api)

### Merged Schema Requirements
Create new migration `V4__MergePodcastApiTables.sql` that adds the podcast_host table to content-api database.

## Feature Slice Architecture

### Target Package Structure
```
apps/content-api/src/main/java/nl/infosupport/agenttalks/
├── content/                    # Content management feature slice
│   ├── graph/                  # GraphQL endpoints and schema
│   ├── model/                  # Content entities (ContentSubmission, PodcastEpisode)
│   ├── eventbus/              # Event publishing
│   └── service/               # Content business logic
├── podcast/                   # Podcast generation feature slice
│   ├── service/               # PodcastOrchestrator, PodcastScriptGenerator, AudioProcessor
│   ├── model/                 # PodcastHost, PodcastScript, PodcastSection
│   ├── clients/               # External clients (Buzzsprout, ElevenLabs)
│   └── processing/            # Temporal workflow components
├── reader/                    # Content reading/scraping feature slice
│   ├── service/               # ContentSummarizer
│   ├── processing/            # ContentProcessor, download/summarization steps
│   ├── model/                 # Reader-specific models
│   └── exceptions/            # Reader-specific exceptions
└── shared/                    # Shared utilities and configurations
    ├── eventbus/              # Vert.x event bus configuration
    ├── config/                # Common configurations
    └── utils/                 # Utility classes
```

## Dependency Consolidation

### Dependencies to Merge (from podcast-api and reader-api)

#### AI and Machine Learning
```xml
<!-- Semantic Kernel (from podcast-api) -->
<dependency>
    <groupId>com.microsoft.semantic-kernel</groupId>
    <artifactId>semantickernel-core</artifactId>
    <version>1.4.4-RC1</version>
</dependency>

<!-- LangChain4J Azure OpenAI (from both) -->
<dependency>
    <groupId>io.quarkiverse.langchain4j</groupId>
    <artifactId>quarkus-langchain4j-azure-openai</artifactId>
    <version>1.0.2</version>
</dependency>
```

#### Content Processing (from reader-api)
```xml
<!-- Apache Tika for content extraction -->
<dependency>
    <groupId>io.quarkiverse.tika</groupId>
    <artifactId>quarkus-tika</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- Apache POI for document processing -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.4.0</version>
</dependency>

<!-- HTTP client for content downloading -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-apache-httpclient</artifactId>
</dependency>
```

#### Cloud Services (from podcast-api)
```xml
<!-- Azure Storage Blob -->
<dependency>
    <groupId>io.quarkiverse.azureservices</groupId>
    <artifactId>quarkus-azure-storage-blob</artifactId>
    <version>1.1.6</version>
</dependency>
```

#### REST/HTTP Clients (from podcast-api)
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client-jackson</artifactId>
</dependency>
```

#### Vert.x Event Bus
```xml
<!-- Add Vert.x support -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-vertx</artifactId>
</dependency>
```

### Remove RabbitMQ Dependencies
```xml
<!-- REMOVE these dependencies -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-messaging-rabbitmq</artifactId>
</dependency>
```

## Event System Migration: RabbitMQ → Vert.x EventBus

### Current RabbitMQ Implementation

#### Content API (Publisher)
```java
// apps/content-api/src/main/java/nl/infosupport/agenttalks/content/eventbus/EventPublisher.java
@ApplicationScoped
public class EventPublisher {
    @Inject
    @Channel("contentSubmissionCreated")
    Emitter<ContentSubmissionCreated> contentSubmissionCreatedEmitter;

    public void publishContentSubmissionCreated(ContentSubmissionCreated event) {
        contentSubmissionCreatedEmitter.send(event);
    }
}
```

#### Reader API (Consumer)
```java
// apps/reader-api/src/main/java/nl/infosupport/agenttalks/reader/processing/ContentProcessor.java
@ApplicationScoped
public class ContentProcessor {
    @Incoming("content-processing-input")
    @ActivateRequestContext
    public Uni<Void> processContentSubmission(ContentSubmissionCreated event) {
        // Processing logic
    }
}
```

### Target Vert.x EventBus Implementation

#### Event Publisher (Merged Content Feature)
```java
// apps/content-api/src/main/java/nl/infosupport/agenttalks/shared/eventbus/InternalEventPublisher.java
@ApplicationScoped
public class InternalEventPublisher {
    @Inject
    EventBus eventBus;

    public void publishContentSubmissionCreated(ContentSubmissionCreated event) {
        eventBus.publish("content.submission.created", event);
    }
}
```

#### Event Consumer (Reader Feature Slice)
```java
// apps/content-api/src/main/java/nl/infosupport/agenttalks/reader/processing/ContentProcessor.java
@ApplicationScoped
public class ContentProcessor {
    @ConsumeEvent("content.submission.created")
    public Uni<Void> processContentSubmission(ContentSubmissionCreated event) {
        // Processing logic - same as before
    }
}
```

### Event Address Mapping
```
RabbitMQ Exchange/Queue → Vert.x EventBus Address
content.submissions.created.v1 → content.submission.created
podcast.generation.requested → podcast.generation.requested
content.summary.completed → content.summary.completed
```

## Direct Feature Integration

### Remove GraphQL Client Proxies

#### Current Inter-Service Communication
```java
// podcast-api calling content-api via GraphQL client
@GraphQLClientApi(configKey = "content-api")
public interface ContentClient {
    @Mutation
    ErrorOr<ContentSubmission> markForProcessing(MarkForProcessing input);
    
    @Query
    List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate);
}
```

#### Target Direct Service Calls
```java
// Direct service injection within same application
@ApplicationScoped
public class PodcastOrchestrator {
    @Inject
    ContentService contentService;  // Direct injection instead of GraphQL client

    public void generatePodcast() {
        List<ContentSubmission> submissions = contentService.findProcessableSubmissions(startDate, endDate);
        contentService.markForProcessing(submissionId);
        // Rest of logic
    }
}
```

### Service Interface Extraction
Create shared service interfaces that both GraphQL layer and internal services can use:

```java
// apps/content-api/src/main/java/nl/infosupport/agenttalks/content/service/ContentService.java
@ApplicationScoped
public class ContentService {
    public List<ContentSubmission> findProcessableSubmissions(LocalDate startDate, LocalDate endDate) {
        return ContentSubmission.findProcessableSubmissions(startDate, endDate);
    }

    public ContentSubmission markForProcessing(Long id) {
        // Implementation
    }

    public ContentSubmission summarizeContent(String url, String summary) {
        // Implementation
    }
}
```

## Migration Implementation Steps

### Phase 1: Database Migration
1. **Create merged migration script**
   - File: `apps/content-api/src/main/resources/db/migration/V4__MergePodcastApiTables.sql`
   - Add podcast_host table with seed data

### Phase 2: Move Code to Feature Slices
2. **Move podcast-api code**
   - Copy `apps/podcast-api/src/main/java/nl/infosupport/agenttalks/podcast/` 
   - To `apps/content-api/src/main/java/nl/infosupport/agenttalks/podcast/`
   - Preserve package structure and all classes

3. **Move reader-api code**
   - Copy `apps/reader-api/src/main/java/nl/infosupport/agenttalks/reader/`
   - To `apps/content-api/src/main/java/nl/infosupport/agenttalks/reader/`
   - Remove GraphQL client dependencies

### Phase 3: Update Dependencies
4. **Merge pom.xml dependencies**
   - Add all unique dependencies from podcast-api and reader-api
   - Remove RabbitMQ messaging dependencies
   - Add Vert.x dependencies

### Phase 4: Event System Migration
5. **Replace RabbitMQ with Vert.x EventBus**
   - Update `EventPublisher` to use Vert.x EventBus
   - Replace `@Incoming` annotations with `@ConsumeEvent`
   - Update application.properties configuration

### Phase 5: Direct Service Integration
6. **Remove GraphQL client proxies**
   - Extract business logic to service classes
   - Replace GraphQL client calls with direct service injection
   - Update podcast and reader components

### Phase 6: Configuration Updates
7. **Update application.properties**
   - Remove RabbitMQ configuration
   - Update ports and service names
   - Merge environment-specific configurations

### Phase 7: Test Migration
8. **Move and update tests**
   - Copy test classes to appropriate feature slice packages
   - Update test configurations for single application
   - Ensure all functionality works with in-memory event bus

## Configuration Changes

### Remove RabbitMQ Configuration
```properties
# REMOVE from application.properties
mp.messaging.outgoing.contentSubmissionCreated.connector=smallrye-rabbitmq
mp.messaging.outgoing.contentSubmissionCreated.exchange.name=content.submissions.created.v1
mp.messaging.incoming.content-processing-input.connector=smallrye-rabbitmq
# ... other RabbitMQ config
```

### Add Vert.x Configuration
```properties
# ADD to application.properties
# Vert.x is enabled by default when dependency is present
# EventBus clustering disabled (single application)
quarkus.vertx.cluster.clustered=false
```

### Update Port Configuration
```properties
# content-api remains on port 8080
quarkus.http.port=8080
quarkus.http.test-port=9080

# Remove other API port configurations since they're merged
```

## Resource File Migrations

### Move Static Resources
- Copy `apps/reader-api/src/main/resources/prompts/` to `apps/content-api/src/main/resources/prompts/`
- Copy any podcast-api specific resources

### Merge Application Properties
- Combine unique configuration from all three applications
- Remove service-specific URLs and inter-service communication config

## Testing Strategy

### Unit Tests

- Copy all test classes to feature slice packages
- Update package imports
- Ensure test isolation with `@QuarkusTest`

### Integration Tests

- Create comprehensive integration tests that verify:
  - Event bus message flow
  - Direct service calls work correctly
  - Database operations across all features
  - External service integrations (Azure OpenAI, Buzzsprout)

### Test Configuration

```properties
# src/test/resources/application.properties
%test.quarkus.datasource.db-kind=h2
%test.quarkus.vertx.cluster.clustered=false

# Mock external services in tests
%test.quarkus.langchain4j.azure-openai.api-key=mock-key
%test.quarkus.azure.storage.blob.connection-string=mock-connection
```

## Validation Gates

### Compilation Validation

```bash
cd apps/content-api
./mvnw clean compile
```

### Test Validation

```bash
cd apps/content-api
./mvnw clean test
```

### Integration Test Validation

```bash
cd apps/content-api
./mvnw clean verify
```

### Application Startup Validation

```bash
cd apps/content-api
./mvnw quarkus:dev
# Verify all endpoints are available:
# - GraphQL UI: http://localhost:8080/q/graphql-ui
# - Quarkus Dev UI: http://localhost:8080/q/dev/
```

### Functional Validation

1. **Content Submission Flow**
   - Submit content via GraphQL
   - Verify event is published to reader feature
   - Verify content is downloaded and summarized
   - Verify summary is stored

2. **Podcast Generation Flow**
   - Trigger podcast generation
   - Verify content is processed by podcast feature
   - Verify audio generation and upload to Buzzsprout

### Database Migration Validation

```bash
# Verify migrations run successfully
./mvnw quarkus:dev
# Check database contains all expected tables
```

## Error Handling Strategies

### Event Bus Error Handling

```java
@ConsumeEvent(value = "content.submission.created", blocking = true)
public void processContentSubmission(ContentSubmissionCreated event) {
    try {
        // Processing logic
    } catch (Exception e) {
        logger.error("Failed to process content submission: " + event.getId(), e);
        // Consider dead letter pattern or retry mechanism
        throw e; // This will be handled by Vert.x error handlers
    }
}
```

### Service Layer Error Handling

- Maintain existing exception hierarchy
- Add feature-specific exceptions where needed
- Ensure proper transaction boundaries

### External Service Resilience

- Keep existing fault tolerance annotations (`@Retry`, `@CircuitBreaker`)
- Verify Azure OpenAI and Buzzsprout integrations remain resilient

## Performance Considerations

### Memory Usage

- Single application will use more memory than three separate services
- Monitor heap usage in development and production
- Consider adjusting JVM parameters if needed

### Event Bus Performance

- Vert.x EventBus is significantly faster than RabbitMQ for local communication
- No network overhead for inter-feature communication
- Better throughput and lower latency expected

### Database Connections

- Single application means fewer database connections overall
- May need to adjust connection pool size in production

## Deployment Updates Required

### Infrastructure Changes

- Update `infra/main.bicep` to remove reader-api and podcast-api modules
- Update the github actions in `.github/workflows/ci.yml` to only build the content-api and the dashboard application
- Adjust resource allocation for single, larger container

### Environment Variables

- Merge environment variables from all three applications
- Remove inter-service communication URLs
- Keep external service configurations (Azure OpenAI, Buzzsprout)
