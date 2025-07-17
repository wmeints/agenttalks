package nl.infosupport.agenttalks.content.model;

import io.micrometer.common.lang.NonNull;

public class ApplicationStatistics {
    @NonNull
    public long totalEpisodes;

    @NonNull
    public long submissionsLastWeek;

    public ApplicationStatistics(long totalEpisodes, long submissionsLastWeek) {
        this.totalEpisodes = totalEpisodes;
        this.submissionsLastWeek = submissionsLastWeek;
    }
}
