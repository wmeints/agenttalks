package nl.infosupport.agenttalks.summarization;

public class ContentSummarizationData {
    public String contentType;
    public String body;
    public String title;
    public String url;
    public String summary;

    public ContentSummarizationData() {
        // Default constructor for serialization/deserialization
    }

    public ContentSummarizationData(String url, String body, String contentType) {
        this.url = url;
        this.body = body;
        this.contentType = contentType;
    }

    public ContentSummarizationData withSummary(String title, String summary) {
        this.title = title;
        this.summary = summary;

        return this;
    }
}
