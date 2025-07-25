package nl.infosupport.agenttalks.podcast.elevenlabs;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.temporal.ChronoUnit;


@RegisterRestClient(baseUri="https://api.elevenlabs.io")
public interface ElevenLabsClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/v1/text-to-speech/{voiceId}")
    @ClientHeaderParam(name = "xi-api-key", value = "${agenttalks.elevenlabs.api-key}")
    @Retry(delay = 1, delayUnit = ChronoUnit.MINUTES, maxRetries = 5)
    public Response createSpeech(@PathParam("voiceId") String voiceId, @QueryParam("output_format") String outputFormat,
            CreateSpeechRequest request);

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        Logger logger = Logger.getLogger(ElevenLabsClient.class);

        logger.error(String.format(
                "Error occurred while calling ElevenLabs API (status: %d): %s", response.getStatus(),
                response.readEntity(String.class)));

        return new RuntimeException("ElevenLabs API call failed");
    }
}
