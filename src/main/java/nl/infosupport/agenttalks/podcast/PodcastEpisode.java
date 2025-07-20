package nl.infosupport.agenttalks.podcast;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.eclipse.microprofile.graphql.NonNull;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity(name = "podcast_episode")
public class PodcastEpisode extends PanacheEntityBase {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NonNull
    @Column(name = "title", nullable = false, columnDefinition = "varchar(500)")
    public String title;

    @NonNull
    @Column(name = "audio_file", columnDefinition = "varchar(1000)")
    public String audioFilePath;

    @NonNull
    @Column(name = "script", columnDefinition = "jsonb", nullable = true)
    @Type(JsonType.class)
    public PodcastScript script;

    @NonNull
    @Column(name = "date_created", columnDefinition = "timestamp")
    public LocalDateTime dateCreated;

    @NonNull
    @Column(name = "episode_number", nullable = false)
    public int episodeNumber;

    @NonNull
    @Column(name = "show_notes", columnDefinition = "text", nullable = false)
    public String description;

    @NonNull
    @Column(name = "description", columnDefinition = "text", nullable = false)
    public String summary;

    public PodcastEpisode() {

    }

    public PodcastEpisode(PodcastScript script, String audioFilePath, int episodeNumber, String showNotes,
            String description) {
        this.title = script.title;
        this.script = script;
        this.audioFilePath = audioFilePath;
        this.episodeNumber = episodeNumber;
        this.description = showNotes;
        this.summary = description;
        this.dateCreated = LocalDateTime.now();
    }

    public static int getNextEpisodeNumber() {
        try {
            // Try to use the sequence for PostgreSQL
            return ((Number) getEntityManager()
                    .createNativeQuery("SELECT nextval('podcast_episode_number_seq')")
                    .getSingleResult()).intValue();
        } catch (Exception e) {
            try {
                // Try H2 sequence syntax
                return ((Number) getEntityManager()
                        .createNativeQuery("SELECT next value for podcast_episode_number_seq")
                        .getSingleResult()).intValue();
            } catch (Exception e2) {
                // Fallback to manual approach if sequences aren't available
                return (int) PodcastEpisode.find("ORDER BY episodeNumber DESC").firstResultOptional()
                        .map(episode -> ((PodcastEpisode) episode).episodeNumber + 1)
                        .orElse(1);
            }
        }
    }
}
