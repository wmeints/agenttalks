# Constraints

This section contains the constraints for the system.

## Organizational constraints

- The system must be accessible only to users of one organization. This is not a public application.

## Technical constraints

- We use Azure for hosting and the LLM model
- We use Dapr Workflow and Agents for the implementation of the processes in the solution.
- We use Dapr in general for any distributed application concerns.
- Only REST APIs can be used for the backend pieces of the solution.
- We use OpenTelemetry for observability of the solution.
