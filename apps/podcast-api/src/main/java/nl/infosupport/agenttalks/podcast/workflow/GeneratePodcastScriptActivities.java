package nl.infosupport.agenttalks.podcast.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.infosupport.agenttalks.podcast.model.PodcastScript;

@ActivityInterface
public interface GeneratePodcastScriptActivities {
    @ActivityMethod
    PodcastScript generatePodcastScript(GeneratePodcastScriptInput input);
}
