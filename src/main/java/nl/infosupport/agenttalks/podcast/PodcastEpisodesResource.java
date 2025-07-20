package nl.infosupport.agenttalks.podcast;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.panache.common.Sort;

@GraphQLApi
public class PodcastEpisodesResource {
    @Query
    @NonNull
    @Description("Finds all podcast episodes")
    public PodcastEpisodeResultSet episodes(int pageIndex, int pageSize) {
        List<PodcastEpisode> items = PodcastEpisode
                .findAll(Sort.by("dateCreated").descending())
                .page(pageIndex, pageSize).list();

        var totalItems = (int) PodcastEpisode.count();

        return new PodcastEpisodeResultSet(items, totalItems, pageIndex, pageSize);
    }
}
