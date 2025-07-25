package nl.infosupport.agenttalks.reader;

public class ContentDownload {
    public String contentType;
    public String body;
    public long contentSubmissionId;

    public ContentDownload() {
        // Default constructor for deserialization
    }

    public ContentDownload(long contentSubmissionId, String contentType, String body) {
        this.contentSubmissionId = contentSubmissionId;
        this.contentType = contentType;
        this.body = body;
    }
}
