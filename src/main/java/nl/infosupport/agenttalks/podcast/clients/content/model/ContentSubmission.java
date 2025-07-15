package nl.infosupport.agenttalks.podcast.clients.content.model;

public class ContentSubmission {
    public Long id;
    public String title;
    public String summary;
    public SubmissionStatus status;
    public String url;

    public ContentSubmission() {

    }

    public ContentSubmission(Long id, String title, String summary, SubmissionStatus status, String url) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.status = status;
        this.url = url;
    }
}
