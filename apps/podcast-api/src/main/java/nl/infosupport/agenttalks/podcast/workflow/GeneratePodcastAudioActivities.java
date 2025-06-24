package nl.infosupport.agenttalks.podcast.workflow;

import java.util.List;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.infosupport.agenttalks.podcast.model.PodcastFragment;

@ActivityInterface
public interface GeneratePodcastAudioActivities {
    @ActivityMethod
    String generateSpeech(PodcastFragment fragment);

    @ActivityMethod
    String concatenateAudioFragments(List<String> audioFragments);

    @ActivityMethod
    String mixPodcastEpisode(String contentAudioFile);
}
