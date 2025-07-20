package nl.infosupport.agenttalks.content;

import java.time.LocalDateTime;

import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Type
@Entity(name = "content_submission")
public class ContentSubmission extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NonNull
    @Column(name = "url", nullable = false, columnDefinition = "varchar(1000)")
    public String url;

    @Column(name = "title", nullable = true, columnDefinition = "varchar(500)")
    public String title;

    @Column(name = "summary", columnDefinition = "text", nullable = true)
    public String summary;

    @NonNull
    @Column(name = "date_created", columnDefinition = "timestamp", nullable = false)
    public LocalDateTime dateCreated;

    @Column(name = "date_modified", columnDefinition = "timestamp", nullable = true)
    public LocalDateTime dateModified;

    @NonNull
    @Column(name = "submission_status", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    public SubmissionStatus status;

    @Column(name = "instructions", columnDefinition = "text", nullable = true)
    public String instructions;

    protected ContentSubmission() {

    }

    public ContentSubmission(String url, String instructions) {
        this.url = url;
        this.instructions = instructions;
        this.dateCreated = LocalDateTime.now();
        this.status = SubmissionStatus.SUBMITTED;
    }

    public void summarize(String title, String summary) {
        this.title = title;
        this.summary = summary;
        this.status = SubmissionStatus.SUMMARIZED;
        this.dateModified = LocalDateTime.now();
    }

    public void markForProcessing() {
        if (status != SubmissionStatus.SUMMARIZED) {
            throw new IllegalStateException("Can only mark for processing after summarizing");
        }

        this.status = SubmissionStatus.PROCESSING;
        this.dateModified = LocalDateTime.now();
    }

    public void markAsProcessed() {
        if (status != SubmissionStatus.PROCESSING) {
            throw new IllegalStateException("Can only mark as processed after processing");
        }

        this.status = SubmissionStatus.PROCESSED;
        this.dateModified = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ContentSubmission{" + "id=" + id + ", url=" + url + ", status=" + status.toString() + "}";

    }

    public static PanacheQuery<ContentSubmission> findPending() {
        var queryParams = Parameters.with("status", SubmissionStatus.SUMMARIZED);
        return find("status = :status", Sort.by("dateCreated").descending(), queryParams);
    }
}
