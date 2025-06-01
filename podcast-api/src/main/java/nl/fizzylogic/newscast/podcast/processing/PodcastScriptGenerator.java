package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.agent.PodcastScriptGenerationAgent;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

/**
 * Generates a script for a podcast episode based on the provided content
 * submissions.
 */
@ApplicationScoped
public class PodcastScriptGenerator {
    @Inject
    PodcastScriptGenerationAgent podcastScriptGenerationAgent;

    @ActivateRequestContext
    @Incoming("script-generation-input")
    @Outgoing("script-generation-output")
    public PodcastEpisodeData process(PodcastEpisodeData episodeData) {
        return episodeData.withPodcastScript("Test");
    }
}
