package nl.infosupport.agenttalks.statistics;

import nl.infosupport.agenttalks.content.ContentSubmission;
import nl.infosupport.agenttalks.podcast.PodcastEpisode;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class ApplicationStatisticsResource {
    @Query
    public @NonNull ApplicationStatistics statistics() {
        var recentSubmissions = ContentSubmission.findPending().count();
        var totalEpisodes = PodcastEpisode.count();

        var statistics = new ApplicationStatistics(totalEpisodes, recentSubmissions);

        return statistics;
    }
}
