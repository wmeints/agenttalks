package nl.infosupport.agenttalks.podcast.workflow;

import java.util.List;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.infosupport.agenttalks.podcast.clients.content.model.ContentSubmission;
import nl.infosupport.agenttalks.podcast.clients.content.model.PodcastEpisode;

@ActivityInterface
public interface ContentMetadataActivities {
    @ActivityMethod
    void lockContentSubmissions(List<ContentSubmission> contentSubmissions);

    @ActivityMethod
    void markContentSubmissionsAsProcessed(List<ContentSubmission> contentSubmissions);

    @ActivityMethod
    PodcastEpisode savePodcastEpisode(String title, String audioFilePath, List<ContentSubmission> contentSubmissions);
}
