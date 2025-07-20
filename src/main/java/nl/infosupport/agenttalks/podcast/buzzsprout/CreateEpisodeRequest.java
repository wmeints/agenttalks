package nl.infosupport.agenttalks.podcast.buzzsprout;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEpisodeRequest {
    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("summary")
    public String summary;

    @JsonProperty("audio_url")
    public String audioUrl;

    @JsonProperty("published")
    public Boolean published = true;

    @JsonProperty("explicit")
    public Boolean explicit = false;

    public CreateEpisodeRequest() {
        // Default constructor for serialization
    }

    public CreateEpisodeRequest(String title, String description, String summary, String audioUrl) {
        this.title = title;
        this.description = description;
        this.summary = summary;
        this.audioUrl = audioUrl;
    }
}