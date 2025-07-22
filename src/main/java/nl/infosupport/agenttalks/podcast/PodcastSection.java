package nl.infosupport.agenttalks.podcast;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

        if (this == o) {
            return true;
        }

        return new EqualsBuilder()
                .append(title, that.title)
                .append(fragments, that.fragments)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(title)
                .append(fragments)
                .toHashCode();
    }
}
