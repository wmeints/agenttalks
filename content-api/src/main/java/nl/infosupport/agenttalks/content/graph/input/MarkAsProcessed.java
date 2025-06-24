package nl.infosupport.agenttalks.content.graph.input;

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
