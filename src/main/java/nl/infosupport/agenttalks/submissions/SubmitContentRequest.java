package nl.infosupport.agenttalks.submissions;

import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.graphql.Input;

@Input
public class SubmitContentRequest {
    @NotEmpty
    String url;
    String instructions;

    public SubmitContentRequest() {

    }

    public SubmitContentRequest(String url, String instructions) {

        this.url = url;
        this.instructions = instructions;
    }
}
