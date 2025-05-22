"""Workflow activities."""

from agenttalks.podcasts.workflow.activities.audio import (
    ConcatenateAudioActivityInput,
    ConcatenateAudioActivityResult,
    concatenate_audio_activity,
)
from agenttalks.podcasts.workflow.activities.buzzsprout import (
    PublishToBuzzsproutActivityInput,
    PublishToBuzzsproutActivityResult,
    publish_to_buzzsprout_activity,
)
from agenttalks.podcasts.workflow.activities.content import (
    CollectSummariesActivityInput,
    CollectSummariesActivityResult,
    SavePodcastMetadataActivityInput,
    SavePodcastMetadataActivityResult,
    collect_summaries_activity,
    save_podcast_metadata_activity,
)
from agenttalks.podcasts.workflow.activities.generation import (
    GeneratePodcastAudioActivityInput,
    GeneratePodcastAudioActivityResult,
    generate_podcast_audio_activity,
)
from agenttalks.podcasts.workflow.activities.script import (
    GeneratePodcastScriptActivityInput,
    GeneratePodcastScriptActivityResult,
    generate_podcast_script_activity,
)
from agenttalks.podcasts.workflow.activities.storage import (
    DownloadIntroActivityInput,
    DownloadIntroActivityResult,
    DownloadOutroActivityInput,
    DownloadOutroActivityResult,
    StorePodcastAudioActivityInput,
    StorePodcastAudioActivityResult,
    download_intro_activity,
    download_outro_activity,
    store_podcast_audio_activity,
)

__all__ = [
    "ConcatenateAudioActivityInput",
    "ConcatenateAudioActivityResult",
    "concatenate_audio_activity",
    "PublishToBuzzsproutActivityInput",
    "PublishToBuzzsproutActivityResult",
    "publish_to_buzzsprout_activity",
    "CollectSummariesActivityInput",
    "CollectSummariesActivityResult",
    "SavePodcastMetadataActivityInput",
    "SavePodcastMetadataActivityResult",
    "collect_summaries_activity",
    "save_podcast_metadata_activity",
    "GeneratePodcastAudioActivityInput",
    "GeneratePodcastAudioActivityResult",
    "generate_podcast_audio_activity",
    "GeneratePodcastScriptActivityInput",
    "GeneratePodcastScriptActivityResult",
    "generate_podcast_script_activity",
    "DownloadIntroActivityInput",
    "DownloadIntroActivityResult",
    "DownloadOutroActivityInput",
    "DownloadOutroActivityResult",
    "StorePodcastAudioActivityInput",
    "StorePodcastAudioActivityResult",
    "download_intro_activity",
    "download_outro_activity",
    "store_podcast_audio_activity",
]
