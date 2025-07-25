package nl.infosupport.agenttalks.podcast;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        if (!(o instanceof PodcastScript that))
            return false;

        if (this == o) {
            return true;
        }

        return new EqualsBuilder()
                .append(title, that.title)
                .append(sections, that.sections)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(title)
                .append(sections)
                .toHashCode();
    }
}
