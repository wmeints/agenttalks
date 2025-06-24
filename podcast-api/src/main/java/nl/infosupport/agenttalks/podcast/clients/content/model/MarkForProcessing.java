package nl.infosupport.agenttalks.podcast.clients.content.model;

import org.eclipse.microprofile.graphql.Input;

@Input
public class MarkForProcessing {
    public long id;

    public MarkForProcessing() {
        // Default constructor for deserialization
    }

    public MarkForProcessing(long id) {
        this.id = id;
    }
}
