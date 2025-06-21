# Building block view

## System context

```mermaid
C4Context
    title Technical context for Newscast

    Person(user, "User", "Accesses the application to consume news content.")
    Person(admin, "Administrator", "Manages and operates the application.")

    System(app, "Newscast", "Delivers a weekly podcast based on submitted content")
    System_Ext(llm, "Azure OpenAI", "Hosts the LLM for the application")
    System_Ext(tts, "ElevenLabs", "Hosts the TTS services for the application")

    Rel(user, app, "HTTPS")
    Rel(admin, app, "HTTPS")
    Rel(app, llm, "HTTPS")
    Rel(app, tts, "HTTPS")

    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```

## Newscast services

```mermaid
C4Container    
    title "Newscast services"

    Container_Boundary(app, "Newscast") {
        Container(spa, "Dashboard", "Sveltekit")
        Container(content, "Content Service", "Quarkus")
        Container(reader, "Reader Service", "Quarkus")
        Container(podcast, "Podcast Service", "Quarkus")
        ContainerDb(content_db, "Content Database", "PostgreSQL")
        Container(broker, "Message Broker", "RabbitMQ")
    }

    System_Ext(llm, "Azure OpenAI", "Hosts the LLM for the application")
    System_Ext(tts, "ElevenLabs", "Hosts the TTS services for the application")
    System_Ext(buzzsprout, "Buzzsprout", "Enables Podcast publication")

    Rel(spa, content, "Uses")
    Rel(content, content_db, "Uses")

    Rel(content, broker, "Publish/Subscribe")
    Rel(reader, broker, "Publish/Subscribe")
    Rel(podcast, broker, "Publish/Subscribe")

    Rel(reader, llm, "Uses", "HTTPS")

    Rel(podcast, llm, "Uses", "HTTPS")
    Rel(podcast, tts, "Uses", "HTTPS")
    Rel(podcast, buzzsprout, "Uses", "HTTPS")

    UpdateLayoutConfig($c4ShapeInRow="4")
```

| Name        | Description                                                   |
| ----------- | ------------------------------------------------------------- |
| Dashboard   | Provides insights into the current week and previous episodes |
| Content API | Stores collected content and generated podcast episodes       |
| Reader API  | Scrapes content and summarizes it for the upcoming podcast    |
| Podcast API | Generates the weekly podcast every friday at 6PM              |
| RabbitMQ    | Eventbus implementation for the application                   |
