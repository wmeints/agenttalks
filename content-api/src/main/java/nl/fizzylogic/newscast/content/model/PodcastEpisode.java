package nl.fizzylogic.newscast.content.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class PodcastEpisode extends PanacheEntity {
    public String title;
    public String audioFilePath;
    public String script;
    public LocalDateTime dateCreated;

    public PodcastEpisode() {

    }

    public PodcastEpisode(String title, String audioFilePath) {
        this.title = title;
        this.audioFilePath = audioFilePath;
        this.dateCreated = LocalDateTime.now();
    }
}
