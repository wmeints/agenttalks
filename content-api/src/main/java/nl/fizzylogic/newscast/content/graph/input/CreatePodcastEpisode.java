package nl.fizzylogic.newscast.content.graph.input;

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
