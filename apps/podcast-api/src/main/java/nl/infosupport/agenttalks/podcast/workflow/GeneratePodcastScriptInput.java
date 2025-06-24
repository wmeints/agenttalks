package nl.infosupport.agenttalks.podcast.workflow;

import java.util.List;

import nl.infosupport.agenttalks.podcast.clients.content.model.ContentSubmission;

public class GeneratePodcastScriptInput {
    public List<ContentSubmission> contentSubmissions;

    public GeneratePodcastScriptInput() {

    }

    public GeneratePodcastScriptInput(List<ContentSubmission> contentSubmissions) {
        this.contentSubmissions = contentSubmissions;
    }
}
