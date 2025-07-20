package nl.infosupport.agenttalks.podcast;

import com.google.common.base.Objects;

import java.util.List;

public class PodcastSection {
    public String title;
    public List<PodcastFragment> fragments;

    public PodcastSection() {
        // Default constructor for JPA
    }

    public PodcastSection(String title, PodcastFragment... fragments) {
        this.title = title;
        this.fragments = List.of(fragments);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PodcastSection that))
            return false;
        return Objects.equal(title, that.title) && Objects.equal(fragments, that.fragments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, fragments);
    }
}
