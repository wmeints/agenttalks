package nl.fizzylogic.newscast.podcast.model;

import java.time.LocalDate;
import java.util.List;

import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;

public class PodcastEpisodeData {
    public LocalDate startDate;
    public LocalDate endDate;
    public List<ContentSubmission> contentSubmissions;
    public String script;

    public PodcastEpisodeData() {
        // Default constructor for serialization/deserialization
    }

    public PodcastEpisodeData(LocalDate startDate, LocalDate endDate,
            List<ContentSubmission> contentSubmissions) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.contentSubmissions = contentSubmissions;
    }

    public PodcastEpisodeData withPodcastScript(String script) {
        this.script = script;
        return this;
    }

    public static PodcastEpisodeDataBuilder builder() {
        return new PodcastEpisodeDataBuilder();
    }

    public static class PodcastEpisodeDataBuilder {
        private PodcastEpisodeData instance;

        public PodcastEpisodeDataBuilder() {
            this.instance = new PodcastEpisodeData();
        }

        public PodcastEpisodeDataBuilder withStartDate(LocalDate startDate) {
            instance.startDate = startDate;
            return this;
        }

        public PodcastEpisodeDataBuilder withEndDate(LocalDate endDate) {
            instance.endDate = endDate;
            return this;
        }

        public PodcastEpisodeDataBuilder withContentSubmissions(List<ContentSubmission> contentSubmissions) {
            instance.contentSubmissions = contentSubmissions;
            return this;
        }

        public PodcastEpisodeData build() {
            return instance;
        }
    }
}
