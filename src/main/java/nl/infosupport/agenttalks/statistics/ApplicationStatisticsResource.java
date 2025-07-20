package nl.infosupport.agenttalks.statistics;

import nl.infosupport.agenttalks.content.ContentSubmission;
import nl.infosupport.agenttalks.podcast.PodcastEpisode;
import org.eclipse.microprofile.graphql.GraphQLApi;

@GraphQLApi
public class ApplicationStatisticsResource {
    public ApplicationStatistics statistics() {
        var recentSubmissions = ContentSubmission.findPending().count();
        var totalEpisodes = PodcastEpisode.count();

        var statistics = new ApplicationStatistics(totalEpisodes, recentSubmissions);

        return statistics;
    }
}
