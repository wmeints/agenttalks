package nl.infosupport.agenttalks.podcast;

import com.google.common.base.Objects;

import java.util.List;

public class PodcastScript {
    public String title;
    public List<PodcastSection> sections;

    public PodcastScript() {

    }

    public PodcastScript(String title, PodcastSection... sections) {
        this.title = title;
        this.sections = List.of(sections);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PodcastScript that)) return false;
        return Objects.equal(title, that.title) && Objects.equal(sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, sections);
    }
}
