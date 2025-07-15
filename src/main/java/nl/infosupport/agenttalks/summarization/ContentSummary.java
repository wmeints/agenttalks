package nl.infosupport.agenttalks.summarization;

public class ContentSummary {
    String title;
    String summary;

    public ContentSummary() {
        // Default constructor for serialization/deserialization
    }

    public ContentSummary(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
}