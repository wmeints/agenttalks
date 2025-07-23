package nl.infosupport.agenttalks.content;

import org.eclipse.microprofile.graphql.NonNull;

public class SubmitContentRequest {
    @NonNull
    public String url;
    public String instructions;
}
