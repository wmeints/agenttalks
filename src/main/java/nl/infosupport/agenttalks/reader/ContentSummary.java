package nl.infosupport.agenttalks.reader;

public class ContentSummary {
    public String title;
    public String summary;

    public ContentSummary() {
        // Default constructor for serialization/deserialization
    }

    public ContentSummary(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
}
