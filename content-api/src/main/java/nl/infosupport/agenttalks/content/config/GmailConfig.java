package nl.infosupport.agenttalks.content.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "gmail.monitoring")
public interface GmailConfig {
    @WithDefault("false")
    boolean enabled();

    @WithDefault("0 */5 * * * ?")
    String schedule();

    Optional<String> credentialsPath();

    Optional<List<String>> allowedSenders();

    @WithDefault("Quarkus Newscast")
    String applicationName();
}