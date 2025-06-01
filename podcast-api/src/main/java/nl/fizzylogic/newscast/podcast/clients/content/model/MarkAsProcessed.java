package nl.fizzylogic.newscast.podcast.clients.content.model;

import org.eclipse.microprofile.graphql.Input;

@Input
public class MarkAsProcessed {
    public long id;

    public MarkAsProcessed() {
        // Default constructor for deserialization
    }

    public MarkAsProcessed(long id) {
        this.id = id;
    }
}
