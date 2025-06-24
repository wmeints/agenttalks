# Agenttalks application

[![Continuous integration](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml/badge.svg)](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml)

This repository contains a full Quarkus implementation for an AI-driven podcast generator.
You can use this to run your own AI-driven podcast or as a sample on how to use Quarkus
and Langchain4J. Enjoy!

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
./mvnw quarkus:dev
```

The following services can be started this way:

| Service Name | Directory          |
| ------------ | ------------------ |
| Content API  | `apps/content-api` |
| Reader API   | `apps/reader-api`  |
| Podcast API  | `apps/podcast-api` |

For the frontend use the command `npm run dev` from the `apps/dahboard` directory.

## Documentation

* [Architecture documentation](docs/architecture/README.md)
