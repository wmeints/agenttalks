package nl.fizzylogic.newscast.podcast.clients.content.model;

public class ContentSubmission {
    public Long id;
    public String title;
    public String summary;
    public SubmissionStatus status;

    public ContentSubmission() {

    }

    public ContentSubmission(Long id, String title, String summary, SubmissionStatus status) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.status = status;
    }
}
