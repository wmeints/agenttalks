package nl.infosupport.agenttalks.content.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;
import org.eclipse.microprofile.graphql.Type;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Type
@Entity(name = "content_submission")
public class ContentSubmission extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "url", nullable = false, columnDefinition = "varchar(1000)")
    public String url;

    @Column(name = "title", nullable = true, columnDefinition = "varchar(500)")
    public String title;

    @Column(name = "summary", columnDefinition = "text", nullable = true)
    public String summary;

    @Column(name = "date_created", columnDefinition = "timestamp", nullable = false)
    public LocalDateTime dateCreated;

    @Column(name = "date_modified", columnDefinition = "timestamp", nullable = true)
    public LocalDateTime dateModified;

    @Column(name = "submission_status", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
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
        return "ContentSubmission{" + "id=" + id + ", url=" + url + ", status=" + status.toString() + "}";

    }

    public static List<ContentSubmission> findProcessable(LocalDate startDate, LocalDate endDate) {
        var startDateTime = startDate.atStartOfDay();
        var endDateTime = endDate.atStartOfDay().plusDays(1);

        Parameters queryParams = Parameters
                .with("startDate", startDateTime)
                .and("endDate", endDateTime)
                .and("status", SubmissionStatus.SUMMARIZED);

        return list(
                "dateCreated >= :startDate and dateCreated < :endDate and status = :status",
                Sort.by("dateCreated"), queryParams);
    }

    public static List<ContentSubmission> findRecentlySubmitted() {
        var startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        var queryParams = Parameters.with("startDate", startOfWeek.atStartOfDay());

        return list("dateCreated >= :startDate", Sort.by("dateCreated").descending(), queryParams);
    }

    public static long countSubmissionsSinceStartOfWeek() {
        var startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        var queryParams = Parameters.with("startDate", startOfWeek.atStartOfDay());

        return find("dateCreated >= :startDate", queryParams).count();
    }
}
