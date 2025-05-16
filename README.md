# AgentTalks

This repository contains the code for our weekly saturday-morning AI-generated podcast 
that helps us make sense of this week's content.

## Why?

We (@Joopsnijder and @wmeints) frequently send eachother links to interesting reads
about AI, Machine Learning, Technology, Agile Practices, and many more topics. It's hard
to keep up because there's just too much happening in AI and the technology landscape in
general.

We figured that it is probably easier to let AI summarize the content and generate a
podcast that we can listen to on saturday morning while doing other stuff around the
house. So here we are, a podcast generator that uses AI for the summarization, podcast
script generation, and conversion from the script to a podcast show!

## Can I use this software too?

You can, but you're going to have to host the beast yourself. We have included the
deployment scripts and documentation to do so, so it shouldn't be too hard.

## System Requirements

- [Dapr 1.15](https://docs.dapr.io/getting-started/install-dapr-cli/)
- [UV Package Manager](https://docs.astral.sh/uv/)
- [Python 3.12 or higher](https://www.python.org/downloads/)
- [Docker or another container manager](https://www.docker.com/products/docker-desktop/)

## Getting started

Please follow these steps to run the software locally:

1. Synchronize the Python dependencies with the following command:

   ```bash
   uv sync --all-packages
   ```

2. Synchronize the Javascript dependencies with the following command:

   ```bash
   cd apps/frontend
   npm install
   ```

3. Run the application from the root directory of the repository with the command:

   ```bash
   dapr run -f ./dapr.yml
   ```

## Documentation

- [Architecture documentation](./docs/architecture)
