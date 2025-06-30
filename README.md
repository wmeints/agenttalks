# Agenttalks application

[![Continuous integration](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml/badge.svg)](https://github.com/wmeints/quarkus-newscast/actions/workflows/ci.yml)

The purpose of the agenttalks application is to help us gain insights into what's happening in
AI without having to read all the content that's available to us. The application will summarize
content for us and produce a weekly podcast for us to listen to, so we get a quick overview of
about 6 minutes rather than reading for multiple hours. We'll provide sources with
the podcast to access the complete information if we want to learn more
about a particular article.

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
