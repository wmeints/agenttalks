package nl.infosupport.agenttalks.podcast.buzzsprout;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "buzzsprout", baseUri = "https://www.buzzsprout.com/api/")
public interface BuzzsproutClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{podcastId}/episodes")
    @ClientHeaderParam(name = "Authorization", value = "Token token=${agenttalks.buzzsprout.api-key}")
    public CreateEpisodeResponse createEpisode(@PathParam("podcastId") String podcastId, CreateEpisodeRequest request);

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        Logger logger = Logger.getLogger(BuzzsproutClient.class);

        logger.error(String.format(
                "Error occurred while calling Buzzsprout API (status: %d): %s", response.getStatus(),
                response.readEntity(String.class)));

        return new RuntimeException("Buzzsprout API call failed");
    }
}