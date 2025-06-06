package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;
import nl.fizzylogic.newscast.podcast.model.PodcastHost;
import nl.fizzylogic.newscast.podcast.service.PodcastScriptGenerator;

public class PodcastScriptGenerationStep {
    @Inject
    PodcastScriptGenerator podcastScriptGenerator;

    @ActivateRequestContext
    @Incoming("script-generation-input")
    @Outgoing("script-generation-output")
    public PodcastEpisodeData process(PodcastEpisodeData episodeData) {
        var firstPodcastHost = getPodcastHost(0);
        var secondPodcastHost = getPodcastHost(1);

        var podcastScript = podcastScriptGenerator.generatePodcastScript(
                firstPodcastHost.name,
                secondPodcastHost.name,
                firstPodcastHost.styleInstructions,
                secondPodcastHost.styleInstructions,
                getSummarizedContent(episodeData));

        return episodeData.withPodcastScript(podcastScript);
    }

    private PodcastHost getPodcastHost(int index) {
        return PodcastHost.findByIndex(index);
    }

    private String getSummarizedContent(PodcastEpisodeData episodeData) {
        var outputBuilder = new StringBuilder();

        for (var submission : episodeData.contentSubmissions) {
            outputBuilder.append(String.format("%s\n\n", submission.title));
            outputBuilder.append(String.format("%s\n\n", submission.summary));
            outputBuilder.append(String.format("URL: %s\n\n", submission.url));
        }

        return outputBuilder.toString();
    }
}
