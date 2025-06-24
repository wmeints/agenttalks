package nl.infosupport.agenttalks.content.eventbus;

import java.time.LocalDateTime;

public class ContentSubmissionCreated {
    public long contentSubmissionId;
    public String url;
    public LocalDateTime dateCreated;

    public ContentSubmissionCreated() {
        // Default constructor for deserialization
    }

    public ContentSubmissionCreated(long contentSubmissionId, String url, LocalDateTime dateCreated) {
        this.contentSubmissionId = contentSubmissionId;
        this.url = url;
        this.dateCreated = dateCreated;
    }
}
