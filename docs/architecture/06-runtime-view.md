# Runtime view

## Generation of a weekly podcast

The podcast generation workflow is an automated process that runs weekly to create podcast episodes from summarized content submissions. The workflow is implemented using Temporal workflows for reliability and includes several stages from content selection to final publication.

### Workflow Overview

```mermaid
graph TD
    A[Scheduled Trigger<br/>Friday 18:00] --> B[Lock Content Submissions]
    B --> C[Generate Podcast Script<br/>Azure OpenAI]
    C --> D[Generate Speech Audio<br/>ElevenLabs TTS]
    D --> E[Concatenate Audio Fragments]
    E --> F[Mix Final Episode Audio]
    F --> G[Save Episode Metadata]
    G --> H[Publish to Buzzsprout]
    H --> I[Mark Content as Processed]
    I --> J[Cleanup & Complete]

    subgraph "Content Activities"
        B
        G
        I
    end

    subgraph "Script Generation"
        C
    end

    subgraph "Audio Processing"
        D
        E
        F
    end

    subgraph "Publishing"
        H
    end

    style A fill:#e1f5fe
    style C fill:#fff3e0
    style D fill:#f3e5f5
    style E fill:#f3e5f5
    style F fill:#f3e5f5
    style H fill:#e8f5e8
```

**Workflow Steps:**
1. **Content Locking** - Prevents duplicate processing of submissions
2. **Script Generation** - Uses Azure OpenAI to create podcast script from content
3. **Audio Generation** - Converts script to speech using ElevenLabs TTS
4. **Audio Processing** - Concatenates and mixes audio fragments
5. **Episode Publishing** - Saves metadata and uploads to Buzzsprout
6. **Cleanup** - Marks content submissions as processed

## Processing Flow in reader-api

The reader-api processes content submissions through a three-step event-driven pipeline using RabbitMQ messaging. Each step is implemented as a separate Quarkus bean with reactive messaging channels.

```mermaid
graph TD
    A[Content Submission Created Event] --> B[Download Content<br/>Apache HTTP Client]
    B --> C[Parse Content<br/>Apache Tika]
    C --> D[Summarize Content<br/>Azure OpenAI + LangChain4J]
    D --> E[Update Content Submission<br/>GraphQL Mutation]
    E --> F[Content Processing Complete]

    subgraph "Content Download Step"
        B
        C
    end

    subgraph "Content Summarization Step"
        D
    end

    subgraph "Content Update Step"
        E
    end

    style A fill:#e1f5fe
    style D fill:#fff3e0
    style E fill:#e8f5e8
```

**Processing Steps:**
1. **Content Download** - Downloads and parses content from URLs using Apache HTTP Client and Tika
2. **Content Summarization** - Uses Azure OpenAI via LangChain4J to generate title and summary
3. **Content Update** - Updates the content submission in Content API via GraphQL mutation

The process is implemented using RabbitMQ reactive messaging with `@Incoming` and `@Outgoing` annotations for scalable, decoupled processing. Each step includes comprehensive error handling and retry logic for robust operation.
