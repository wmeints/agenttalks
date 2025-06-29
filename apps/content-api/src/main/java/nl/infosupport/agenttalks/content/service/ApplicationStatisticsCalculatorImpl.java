package nl.infosupport.agenttalks.content.service;

import jakarta.enterprise.context.ApplicationScoped;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;
import nl.infosupport.agenttalks.content.model.ApplicationStatistics;

@ApplicationScoped
public class ApplicationStatisticsCalculatorImpl implements ApplicationStatisticsCalculator {
    @Override
    public ApplicationStatistics getApplicationStatistics() {
        var episodesProduced = PodcastEpisode.count();
        var submissionsSinceStartOfWeek = ContentSubmission.countSubmissionsSinceStartOfWeek();

        ApplicationStatistics statistics = new ApplicationStatistics();
        statistics.totalEpisodes = (int) episodesProduced;
        statistics.submissionsLastWeek = (int) submissionsSinceStartOfWeek;

        return statistics;
    }
}
