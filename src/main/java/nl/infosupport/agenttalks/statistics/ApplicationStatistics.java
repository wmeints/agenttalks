package nl.infosupport.agenttalks.statistics;

import org.eclipse.microprofile.graphql.NonNull;

public class ApplicationStatistics {
    @NonNull
    public long totalEpisodes;

    @NonNull
    public long pendingSubmissions;

    public ApplicationStatistics(long totalEpisodes, long pendingSubmissions) {
        this.totalEpisodes = totalEpisodes;
        this.pendingSubmissions = pendingSubmissions;
    }
}
