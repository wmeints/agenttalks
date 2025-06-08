package nl.fizzylogic.newscast.content.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "podcast_episode")
public class PodcastEpisode extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(500)")
    public String title;

    @Column(name = "audio_file", columnDefinition = "varchar(1000)")
    public String audioFilePath;

    @Column(name = "script", columnDefinition = "text", nullable = true)
    public String script;

    @Column(name = "date_created", columnDefinition = "timestamp")
    public LocalDateTime dateCreated;

    public PodcastEpisode() {

    }

    public PodcastEpisode(String title, String audioFilePath) {
        this.title = title;
        this.audioFilePath = audioFilePath;
        this.dateCreated = LocalDateTime.now();
    }
}
