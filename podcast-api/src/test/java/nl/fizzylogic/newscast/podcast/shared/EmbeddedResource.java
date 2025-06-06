package nl.fizzylogic.newscast.podcast.shared;

import java.io.IOException;
import java.io.InputStream;

public class EmbeddedResource {
    public static String read(String resourceName) {
        try (InputStream is = EmbeddedResource.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                return null;
            }

            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
}
