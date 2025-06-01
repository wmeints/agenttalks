package nl.fizzylogic.newscast.reader.model;

public class ContentSummary {
    public long contentSubmissionId;
    public String contentType;
    public String summary;

    public ContentSummary() {
        // Default constructor for serialization/deserialization
    }

    public ContentSummary(long contentSubmissionId, String contentType, String summary) {
        this.contentSubmissionId = contentSubmissionId;
        this.contentType = contentType;
        this.summary = summary;
    }
}
