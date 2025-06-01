# Solution strategy

This section describes the solution strategy. We've listed the major design decisions in
this section with an explanation why we choose a certain direction.

## Using services to implement the business logic

### Implementing the main business processes

We have to main processes running in the application:

| Process                            | Runs                            |
| ---------------------------------- | ------------------------------- |
| Summarization of submitted content | Every time content is submitted |
| Generation of a weekly podcast     | Every week at 18:00             |

We can implement each process in their own service to isolate them and make them more
resilient. Therefore choose to build two services:

| Service | Process                            |
| ------- | ---------------------------------- |
| Reader  | Summarization of submitted content |
| Podcast | Generation of a weekly podcast     |

### Content management

We'll store the content submitted by users and the podcast metadata separately in a
`Content` service. Data is stored in PostgreSQL as this is a well-known open source
database engine with a lot of experienced developers using it on a daily basis.

We'll use [Hibernate Panache](https://thorben-janssen.com/introduction-panache/) to work
with the data in the database. Panache offers the following benefits to us:

- Easy to implement entities using the active record pattern.
- Great support for working with PostgreSQL.

We choose to go with an active record approach as our data requirements are basic. We
have very few complex queries in the application.

## Using Quarkus to implement the services

As this is a Quarkus sample, we choose to use [Quarkus](https://quarkus.io) to implement
services. This does give us a few advantages in production:

- We can run native images speeding up the boot times dramatically.
- We can be more productive thanks to the many developer oriented features in Quarkus.
- We can use an event-driven without much overhead for processing content and generating
  podcasts.

## Using an event-driven approach

### Content submission and summarization

When a user submits content to the application we want the response to be nice and
quick. Summarizing content with an LLM takes a little while, so we choose to make this
process asynchronous. We'll publish an event from the content service that gets picked
up by the reader service triggering the summarization process.

The summarization process itself will use an event-driven orchestration to make it
easier to restart steps in the process should we run into issues with the LLM provider
for example.

### Podcast generation

The podcast generation process is even slower than the content summarization process. So
it makes sense to apply an event-driven approach to this process as well.

We'll use [the built-in scheduler](https://quarkus.io/guides/scheduler-reference) options
for Quarkus to trigger the podcast generation process every friday at 18:00.

### Microprofile reactive messaging

We'll use [the Microprofile Reactive Messaging
standard](https://microprofile.io/specifications/reactive-messaging/) to implement the
event-driven orchestration in the solution.

### RabbitMQ infrastructure

We'll use a RabbitMQ server to facilitate messaging. It's much easier to configure than
Kafka and provides enough performance for what we're building. We can switch to a
different message broker in the future if we want thanks to how reactive messaging works
in Quarkus.

## Using GraphQL

There are cases to be made for using REST as well as GraphQL. We don't have a very
strong opinion on both, but GraphQL does give us a few advantages in this solution:

- Flexible API interface: We can include more or less data in a client without having to
  change the server.
- Versionless API interface: We can deprecate fields in the API without introducing new
  endpoints.

We use [SmallRye](https://smallrye.io/smallrye-graphql/latest/) for the GraphQL
implementation.

