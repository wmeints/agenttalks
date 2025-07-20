package nl.infosupport.agenttalks.podcast;

import com.google.common.base.Objects;

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
        if (!(o instanceof PodcastFragment that)) return false;
        return Objects.equal(host, that.host) && Objects.equal(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, content);
    }
}
