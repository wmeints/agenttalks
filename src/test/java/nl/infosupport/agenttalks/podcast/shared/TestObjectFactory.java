package nl.infosupport.agenttalks.podcast.shared;

import java.util.List;

import net.datafaker.Faker;
import nl.infosupport.agenttalks.content.model.ContentSubmission;
import nl.infosupport.agenttalks.content.model.PodcastEpisode;
import nl.infosupport.agenttalks.content.model.SubmissionStatus;
import nl.infosupport.agenttalks.podcast.model.PodcastFragment;
import nl.infosupport.agenttalks.podcast.model.PodcastScript;
import nl.infosupport.agenttalks.podcast.model.PodcastSection;

public class TestObjectFactory {
    private static final Faker faker = new Faker();

    public static PodcastScript createPodcastScript() {
        var script = new PodcastScript();

        var segment = new PodcastSection();

        segment.title = "Test Segment";
        segment.fragments = List.of(
                new PodcastFragment(
                        "Joop Snijder",
                        "Hallo en welkom bij een nieuwe aflevering van Agent talks!"),
                new PodcastFragment(
                        "Willem Meints",
                        "Vandaag gaan we het hebben over de nieuwste ontwikkelingen in AI."));

        script.sections = List.of(segment);

        return script;
    }

    public static ContentSubmission createSummarizedSubmission() {
        var submission = new ContentSubmission("https://example.com/test-url");

        submission.id = faker.number().randomNumber();
        submission.title = faker.lorem().sentence();
        submission.summary = faker.lorem().paragraph();
        submission.status = SubmissionStatus.SUMMARIZED;

        return submission;
    }

    public static PodcastEpisode createPodcastEpisode() {
        return new PodcastEpisode(
                "Test episode",
                "https://test.blob.core.windows.net/episodes/test-audio16895985858398394419.mp3",
                1,
                "Test show notes",
                "This is a test episode description.");
    }
}
