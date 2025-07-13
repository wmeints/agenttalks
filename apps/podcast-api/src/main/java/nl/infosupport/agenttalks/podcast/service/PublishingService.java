package nl.infosupport.agenttalks.podcast.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.BuzzsproutClient;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.model.CreateEpisodeRequest;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.model.CreateEpisodeResponse;

@ApplicationScoped
public class PublishingService {

    @Inject
    @RestClient
    BuzzsproutClient buzzsproutClient;

    @ConfigProperty(name = "buzzsprout.podcast-id")
    String podcastId;

    private static final Logger logger = Logger.getLogger(PublishingService.class);

    @Retry(maxRetries = 3)
    public String publishPodcastEpisode(int seasonNumber, int episodeNumber, String title, String description,
            String showNotes, String audioFileUrl) {
        logger.infof("Publishing podcast episode to Buzzsprout: %s (Season %d, Episode %d)", 
                    title, seasonNumber, episodeNumber);

        try {
            CreateEpisodeRequest request = new CreateEpisodeRequest(
                    seasonNumber, episodeNumber, title, description,
                    showNotes, audioFileUrl);

            CreateEpisodeResponse response = buzzsproutClient.createEpisode(podcastId, request);

            logger.infof("Successfully published episode to Buzzsprout with ID: %d", response.id);

            return response.id.toString();
        } catch (Exception e) {
            logger.errorf(e, "Failed to publish episode to Buzzsprout: %s", title);
            throw new RuntimeException("Failed to publish episode to Buzzsprout", e);
        }
    }
}