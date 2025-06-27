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

You can run the application locally with the Quarkus CLI or by using the npm script provided
with the module. Please find the run commands for each service in the following table:

| Module      | Location           | Run command   |
| ----------- | ------------------ | ------------- |
| Content API | `apps/content-api` | `quarkus dev` |
| Reader API  | `apps/reader-api`  | `quarkus dev` |
| Podcast API | `apps/podcast-api` | `quarkus dev` |
| Dashboard   | `apps/dashboard`   | `npm run dev` |

## Documentation

- [Product documentation](docs/product/README.md)
- [Architecture documentation](docs/architecture/README.md)
