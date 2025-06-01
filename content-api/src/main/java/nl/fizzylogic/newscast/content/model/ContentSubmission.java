package nl.fizzylogic.newscast.content.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.eclipse.microprofile.graphql.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Type
@Entity
public class ContentSubmission extends PanacheEntity {
    @Column(name = "url", nullable = false)
    public String url;

    @Column(name = "title")
    public String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    public String summary;

    @Column(name = "date_created", columnDefinition = "TIMESTAMP")
    public LocalDateTime dateCreated;

    @Column(name = "date_modified", columnDefinition = "TIMESTAMP")
    public LocalDateTime dateModified;

    @Column(name = "status")
    public SubmissionStatus status;

    protected ContentSubmission() {

    }

    public ContentSubmission(String url) {
        this.url = url;
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
        this.status = SubmissionStatus.PROCESSING;
        this.dateModified = LocalDateTime.now();
    }

    public void markAsProcessed() {
        this.status = SubmissionStatus.PROCESSED;
        this.dateModified = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ContentSubmission{" +
                "id=" + id.toString() +
                ", url=" + url +
                ", status=" + status.toString();
    }

    public static List<ContentSubmission> findProcessable(LocalDate startDate, LocalDate endDate) {
        var startDateTime = startDate.atStartOfDay();
        var endDateTime = endDate.atStartOfDay().plusDays(1);

        Parameters queryParams = Parameters.with("startDate", startDateTime)
                .and("endDate", endDateTime).and("status", SubmissionStatus.SUMMARIZED);

        return list(
                "dateCreated >= :startDate and dateCreated < :endDate and status = :status",
                Sort.by("dateCreated"), queryParams);
    }
}
