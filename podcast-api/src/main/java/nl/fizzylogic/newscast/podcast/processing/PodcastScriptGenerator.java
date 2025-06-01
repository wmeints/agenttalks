package nl.fizzylogic.newscast.podcast.processing;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;

@ApplicationScoped
public class PodcastScriptGenerator {
    @Incoming("script-generation-input")
    @Outgoing("script-generation-output")
    public JsonObject process(JsonObject message) {
        var episodeData = message.mapTo(PodcastEpisodeData.class);
        return JsonObject.mapFrom(episodeData.withPodcastScript("Test"));
    }
}
