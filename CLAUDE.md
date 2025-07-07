# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Agenttalks is an AI-driven weekly podcast generator that automatically creates podcasts from submitted content. The system consists of a microservices architecture with three Java/Quarkus backend services and a SvelteKit frontend dashboard.

## Architecture

The system follows a microservices architecture with event-driven communication:

- **Dashboard** (SvelteKit): Frontend for managing content and viewing episodes
- **Content API** (Quarkus): Stores content submissions and podcast episodes via GraphQL
- **Reader API** (Quarkus): Downloads and summarizes content using LLM
- **Podcast API** (Quarkus): Generates weekly podcasts using Temporal workflows
- **RabbitMQ**: Message broker for event-driven communication
- **PostgreSQL**: Database for content storage

## Common Development Commands

### Running Services Locally

Navigate to the specific service directory and run:

| Service     | Directory          | Command       |
|-------------|-------------------|---------------|
| Content API | `apps/content-api` | `quarkus dev` |
| Reader API  | `apps/reader-api`  | `quarkus dev` |
| Podcast API | `apps/podcast-api` | `quarkus dev` |
| Dashboard   | `apps/dashboard`   | `npm run dev` |

### Testing

**Java Services:**
```bash
# Run tests for a specific service
cd apps/content-api
./mvnw test

# Run tests for all Java services from root
./mvnw test
```

**Dashboard:**
```bash
cd apps/dashboard
npm test              # Run all tests
npm run test:unit     # Run unit tests only
```

### Building

**Java Services:**
```bash
# Build specific service
cd apps/content-api
./mvnw package

# Build all services from root
./mvnw package
```

**Dashboard:**
```bash
cd apps/dashboard
npm run build
npm run check     # Type checking
npm run lint      # Linting
```

## Key Technologies

- **Backend**: Java 21, Quarkus 3.23.3, Maven
- **Frontend**: SvelteKit, TypeScript, Tailwind CSS, Vite
- **Database**: PostgreSQL with Flyway migrations
- **Messaging**: RabbitMQ with Quarkus Messaging
- **Workflow**: Temporal for podcast generation workflows
- **AI Integration**: Azure OpenAI for content summarization and script generation
- **TTS**: ElevenLabs for audio generation
- **Monitoring**: OpenTelemetry with Micrometer

## Workflow Processing

The system operates on an event-driven model:

1. **Content Submission**: Users submit content URLs via the dashboard
2. **Content Processing**: Reader API downloads, parses, and summarizes content
3. **Podcast Generation**: Podcast API runs weekly Temporal workflows (Friday 6PM) to generate episodes
4. **Audio Processing**: Script generation → TTS → Audio concatenation → Buzzsprout publishing

## Development Guidelines

- Use Quarkus dev services for local development (automatic PostgreSQL, RabbitMQ setup)
- Follow existing code patterns and naming conventions
- GraphQL is used for content API interactions
- All services use Panache for database operations
- Temporal workflows handle complex podcast generation orchestration
- Each service has its own database schema managed by Flyway