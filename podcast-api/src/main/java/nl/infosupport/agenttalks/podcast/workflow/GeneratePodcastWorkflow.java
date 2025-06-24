package nl.infosupport.agenttalks.podcast.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface GeneratePodcastWorkflow {
    @WorkflowMethod
    void generatePodcast(GeneratePodcastWorkflowInput input);

    @QueryMethod
    PodcastGenerationStatus getStatus();
}
