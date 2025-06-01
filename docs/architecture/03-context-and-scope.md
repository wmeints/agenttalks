# Context and scope

This section lists the context and scope for the solution. Use this section to learn
more about external interfaces that the solution connects with.

## Business context

```mermaid
C4Context
    title Business context for Newscast
    
    Person(user, "User", "Accesses the application to consume news content.")
    Person(admin, "Administrator", "Manages and operates the application.")
    
    System(app, "Newscast", "Delivers a weekly podcast based on submitted content")

    System_Ext(llm, "Azure OpenAI", "Hosts the LLM for the application")
    System_Ext(tts, "ElevenLabs", "Hosts the TTS services for the application")
    System_Ext(buzzsprout, "Buzzsprout", "Enables Podcast publication")

    Rel(user, app, "Uses")
    Rel(admin, app, "Operates")

    Rel(app, llm, "Uses")
    Rel(app, tts, "Uses")
    Rel(app, buzzsprout, "Uses")

    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```

## Technical context

```mermaid
C4Context
    title Technical context for Newscast

    Person(user, "User", "Accesses the application to consume news content.")
    Person(admin, "Administrator", "Manages and operates the application.")

    System(app, "Newscast", "Delivers a weekly podcast based on submitted content")
    System_Ext(llm, "Azure OpenAI", "Hosts the LLM for the application")
    System_Ext(tts, "ElevenLabs", "Hosts the TTS services for the application")
    System_Ext(buzzsprout, "Buzzsprout", "Enables Podcast publication")

    Rel(user, app, "HTTPS")
    Rel(admin, app, "HTTPS")
    Rel(app, llm, "HTTPS")
    Rel(app, tts, "HTTPS")
    Rel(app, buzzsprout, "HTTPS")

    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```
