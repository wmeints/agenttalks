package nl.infosupport.agenttalks.content.graph.input;

import org.eclipse.microprofile.graphql.Input;

@Input
public class SubmitContent {
    public String url;

    public SubmitContent() {

    }

    public SubmitContent(String url) {
        this.url = url;
    }
}
