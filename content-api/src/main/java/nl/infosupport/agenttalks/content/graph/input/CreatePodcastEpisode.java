package nl.infosupport.agenttalks.content.graph.input;

public class CreatePodcastEpisode {
    public String audioFile;
    public String title;
    public String showNotes;
    public String description;

    public CreatePodcastEpisode() {
        // Default constructor for serialization/deserialization
    }

    public CreatePodcastEpisode(String audioFile, String title, String showNotes, String description) {
        this.audioFile = audioFile;
        this.title = title;
        this.showNotes = showNotes;
        this.description = description;
    }
}
