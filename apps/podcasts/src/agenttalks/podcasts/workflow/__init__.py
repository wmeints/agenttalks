"""
Workflow implementation for generating the agenttalks podcast.

This workflow has the following steps:

- Collect summaries from the past week
- Generate a script for the podcast
- Generate audio for the podcast
- Download the intro and outro audio
- Concatenate the audio
- Store the podcast audio
- Save the podcast metadata
- Publish the podcast to Buzzsprout

Each step is implemented as a separate activity so we can restart the workflow at any
point if it fails. This is important because the workflow can take a long time to
complete and we don't want to have to start over from the beginning if something
goes wrong.
"""

from dataclasses import dataclass
from datetime import datetime
from typing import Generator

from dapr.ext.workflow import DaprWorkflowContext

from agenttalks.podcasts.workflow.activities import (
    CollectSummariesActivityInput,
    ConcatenateAudioActivityInput,
    DownloadIntroActivityInput,
    DownloadOutroActivityInput,
    GeneratePodcastAudioActivityInput,
    GeneratePodcastScriptActivityInput,
    PublishToBuzzsproutActivityInput,
    SavePodcastMetadataActivityInput,
    StorePodcastAudioActivityInput,
    collect_summaries_activity,
    concatenate_audio_activity,
    download_intro_activity,
    download_outro_activity,
    generate_podcast_audio_activity,
    generate_podcast_script_activity,
    publish_to_buzzsprout_activity,
    save_podcast_metadata_activity,
    store_podcast_audio_activity,
)
from agenttalks.podcasts.workflow.logging import ReplaySafeLogger
from agenttalks.podcasts.workflow.runtime import workflow_runtime as wfr


@dataclass
class PodcastProductionWorkflowResult:
    """
    The result of the podcast production workflow.

    Attributes
    ----------
    podcast_id : str
        The ID of the podcast episode
    podcast_title : str
        The title of the podcast episode
    podcast_description : str
        The description of the podcast episode
    podcast_audio_url : str
        The URL of the podcast audio file
    buzzsprout_episode_id: str
        The ID of the episode in Buzzsprout
    buzzsprout_episode_url: str
        The URL of the episode in Buzzsprout
    """

    podcast_id: str
    podcast_title: str
    podcast_description: str
    podcast_audio_url: str
    buzzsprout_episode_id: str
    buzzsprout_episode_url: str
    buzzsprout_episode_number: int


@dataclass
class PodcastProductionWorkflowInput:
    """
    Input to the podcast production workflow.

    Attributes
    ----------
    start_date : datetime
        The start date of the week to collect summaries for
    end_date : datetime
        The end date of the week to collect summaries for
    """

    start_date: datetime
    end_date: datetime


@wfr.workflow(name="podcast_production_workflow")
def podcast_production_workflow(
    ctx: DaprWorkflowContext, _input_data: PodcastProductionWorkflowInput
) -> Generator:
    """
    Handle the end-to-end process for creating and publishing podcast content.

    Parameters
    ----------
    ctx : DaprWorkflowContext
        The workflow context provided by Dapr
    input_data : Any
        Data passed to the workflow

    Returns
    -------
    Generator
        The workflow output
    """
    logger = ReplaySafeLogger(ctx)

    _collect_result = yield ctx.call_activity(
        collect_summaries_activity, input=CollectSummariesActivityInput()
    )

    logger.info("Collected this weeks summaries")

    _script_result = yield ctx.call_activity(
        generate_podcast_script_activity, input=GeneratePodcastScriptActivityInput()
    )

    logger.info("Generated podcast script")

    _audio_result = yield ctx.call_activity(
        generate_podcast_audio_activity, input=GeneratePodcastAudioActivityInput()
    )

    logger.info("Generated podcast audio")

    _intro_result = yield ctx.call_activity(
        download_intro_activity, input=DownloadIntroActivityInput()
    )

    logger.info("Downloaded intro audio")

    _outro_result = yield ctx.call_activity(
        download_outro_activity, input=DownloadOutroActivityInput()
    )

    logger.info("Downloaded outro audio")

    _concatenate_result = yield ctx.call_activity(
        concatenate_audio_activity, input=ConcatenateAudioActivityInput()
    )

    logger.info("Produced final audio file")

    _store_result = yield ctx.call_activity(
        store_podcast_audio_activity, input=StorePodcastAudioActivityInput()
    )

    logger.info("Saved podcast to storage")

    _save_metadata_result = yield ctx.call_activity(
        save_podcast_metadata_activity, input=SavePodcastMetadataActivityInput()
    )

    logger.info("Stored metadata for the podcast")

    _publish_result = yield ctx.call_activity(
        publish_to_buzzsprout_activity, input=PublishToBuzzsproutActivityInput()
    )

    logger.info("Published podcast to Buzzsprout")
