package nl.fizzylogic.newscast.podcast.clients.content.model;

public class CreatePodcastEpisode {
    public String audioFile;
    public String title;

    public CreatePodcastEpisode() {
        // Default constructor for serialization/deserialization
    }

    public CreatePodcastEpisode(String audioFile, String title) {
        this.audioFile = audioFile;
        this.title = title;
    }
}
