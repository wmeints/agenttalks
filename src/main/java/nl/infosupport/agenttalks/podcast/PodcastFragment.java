package nl.infosupport.agenttalks.podcast;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PodcastFragment {
    public String host;
    public String content;

    public PodcastFragment() {
        // Default constructor for serialization
    }

    public PodcastFragment(String host, String content) {
        this.host = host;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PodcastFragment that))
            return false;

        if (o == this) {
            return true;
        }

        return new EqualsBuilder()
                .append(host, that.host)
                .append(content, that.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(host)
                .append(content)
                .toHashCode();
    }
}
