package nl.fizzylogic.newscast.podcast.clients.content.model;

import java.time.LocalDateTime;

public class PodcastEpisode {
    public String title;
    public String audioFilePath;
    public LocalDateTime dateCreated;

    public PodcastEpisode() {

    }

    public PodcastEpisode(String title, String audioFilePath) {
        this.title = title;
        this.audioFilePath = audioFilePath;
        this.dateCreated = LocalDateTime.now();
    }
}
