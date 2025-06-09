# Newscast application

[![Continuous integration](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml/badge.svg)](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml)

This repository contains a full Quarkus implementation for an AI-driven podcast generator.
You can use this to run your own AI-driven podcast or as a sample on how to use Quarkus
and Semantic Kernel. Enjoy!

## System requirements

- [Java SDK 21](https://bell-sw.com/pages/downloads/#jdk-21-lts)
- [Docker](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/)
- [VSCode](https://code.visualstudio.com)
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling)

## Getting started

You can run the application locally with the Quarkus CLI or by using a maven target.
It works best if you run each of the services in a separate terminal pane/window from
their directory as listed below. For the Quarkus developer CLI use the following
command to start the service:

```bash
quarkus dev
```

Alternatively you can use this command:

```bash
mvn quarkus:dev
```

The following services can be started this way:

| Service Name | Directory     |
| ------------ | ------------- |
| Content API  | `content-api` |
| Reader API   | `reader-api`  |
| Podcast API  | `podcast-api` |

## Development

### Running services in development mode

You can run one or more services in development mode by using the following command
in one of the `*-api` directories:

```bash
quarkus dev
```

Alternatively you can use `mvn quarkus:dev` if you don't have the Quarkus CLI.

### Submitting content for the podcast

You can test the content submission process through the development UI.
After starting all the services go to http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui
and run the following mutation through the UI:

```graphql
mutation {
  submitContent(input:{url: "https://medium.com/trendyol-tech/rabbitmq-exchange-types-d7e1f51ec825"}) {
    id
  }
}
```

After submitting the content, you can watch it progress through the Jaeger UI: http://localhost:16686

## Documentation

* [Architecture documentation](docs/architecture/README.md)
