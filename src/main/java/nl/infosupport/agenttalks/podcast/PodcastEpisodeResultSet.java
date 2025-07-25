package nl.infosupport.agenttalks.podcast;

import java.util.List;

import nl.infosupport.agenttalks.shared.PagedResultSet;

public class PodcastEpisodeResultSet extends PagedResultSet<PodcastEpisode> {
    public PodcastEpisodeResultSet(List<PodcastEpisode> items, int totalItems, int pageIndex, int pageSize) {
        super(items, totalItems, pageIndex, pageSize);
    }
}
