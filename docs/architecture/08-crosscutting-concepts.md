# Cross-cutting concepts

## Observability

The application uses JBoss Logging as the primary logging framework, which is the
default logging implementation in Quarkus. This provides structured logging capabilities
across all application components.

### Logging Strategy

- **Framework**: JBoss Logging with SLF4J facade
- **Log Levels**: Configured per package/class for appropriate verbosity
- **Structured Logging**: JSON format for production environments to facilitate log
  aggregation
- **Local Development**: Human-readable console output for debugging

### Key Logging Areas

- **Content Processing**: Detailed logs for URL scraping, AI summarization, and content
  storage
- **Event Bus Operations**: Tracking of Vert.x event flows between features
- **External Service Calls**: Request/response logging for AI services (Azure OpenAI),
  TTS (ElevenLabs), and publishing (Buzzsprout)
- **Database Operations**: Hibernate/Panache query logging in development
- **Fault Tolerance**: Circuit breaker state changes and retry attempts

### Future Observability Enhancements

OpenTelemetry integration was temporarily removed due to configuration issues but should
be re-added in future iterations to provide:

- Distributed tracing across service boundaries
- Metrics collection for performance monitoring
- Integration with Azure Application Insights for comprehensive observability

## CI/CD workflow

Building, testing and deploying the application code is done through Github Actions.
We have a specific set up for the workflow. The workflow has the following jobs:

- Build and test
- Deploy to Azure Staging Environment
- Deploy to Azure Production Environment (Not yet implemented)

### Building and testing

The first step in the workflow is used to build and test the components. We run quarkus
tests to verify that the general behavior of the application is correct. At the end of
this job, we push the images to the container registry.

We tag images with `1.0.0-<hash>` to ensure we have images matching the github commit
hash that was used to produce the images.

### Deploying to Azure staging environment

When the build and test job succeeds, we deploy the published container images to the
Azure staging environment.

We manually verify that the installation succeeded on the staging environment.

### Deploying to Azure production environment

We use a manual approval for the staging environment. When the environment is approved,
we deploy the application to the production environment.

## Behavior-Driven Development (BDD) Testing

The application uses Cucumber for BDD testing to ensure that business requirements are
properly validated through executable specifications. This approach bridges the gap
between business stakeholders and technical implementation by using natural language
specifications.

### Test Organization Structure

```text
features/
├── submissions.feature
└── episodes.feature

src/test/java/nl/infosupport/agenttalks/
└── bdd/
    ├── SubmissionSteps.java
    ├── EpisodeSteps.java
    ├── SummarizationSteps.java
    ├── AuthenticationSteps.java
    ├── DatabaseSteps.java
    └── CommonSteps.java
```

### Feature File Conventions

- **One Feature Per File**: Each `.feature` file describes a single business capability
- **Natural Language**: Written in Gherkin syntax using Given-When-Then format
- **Business-Focused**: Scenarios describe user goals and system behavior, not implementation details
- **Data-Driven**: Use scenario outlines and examples for comprehensive test coverage

### Step Definition Organization

Step definitions are organized following the feature slice architecture pattern:

- **Feature Files**: Located in the root `features/` directory, organized by domain folders
- **Step Definitions**: Located in `src/test/java/**/bdd/` within each feature slice package
- **Shared Steps**: Common authentication, database setup, and utility steps in `src/test/java/**/shared/bdd/`
- **Naming Convention**: Step definition classes end with `Steps.java` and match their corresponding feature area

### Integration with Quarkus Testing

BDD tests integrate seamlessly with the existing Quarkus testing infrastructure:

```java
@QuarkusTest
@Cucumber
public class BDDTestRunner {
    // Cucumber test runner for Quarkus integration
}
```

### Key BDD Testing Areas

- **Content Submissions**: User story validation for content submission workflows and automatic AI summarization triggers
- **Podcast Episodes**: Public-facing functionality for browsing and accessing published podcast episodes
- **Authentication & Authorization**: User access control and permission validation
- **Integration Scenarios**: Cross-feature workflows and event-driven processing validation

### Shared Step Definitions

Common step definitions handle cross-cutting concerns:

- **Authentication Steps**: User login, token management, and role-based access
- **Database Steps**: Test data setup, cleanup, and state verification
- **Common Steps**: Shared assertions, time management, and utility operations

### Benefits of BDD Approach

- **Living Documentation**: Feature files serve as executable specifications that stay current
- **Business Alignment**: Non-technical stakeholders can review and validate scenarios
- **Test Maintainability**: Step definitions can be reused across multiple scenarios
- **Integration Testing**: Validates complete user journeys across multiple features
- **Regression Prevention**: Automated execution ensures business requirements remain satisfied
