package nl.infosupport.agenttalks.shared;

import nl.infosupport.agenttalks.content.ContentSubmission;
import nl.infosupport.agenttalks.podcast.PodcastFragment;
import nl.infosupport.agenttalks.podcast.PodcastScript;
import nl.infosupport.agenttalks.podcast.PodcastSection;

public class TestObjectFactory {
    public static ContentSubmission generateContentSubmission() {
        var submission = new ContentSubmission("http://www.example.org", null);
        submission.summarize("Test article", "Test summary");

        return submission;
    }

    public static PodcastScript generatePodcastScript() {
        return new PodcastScript(
                "Test podcast Episode",
                new PodcastSection("Introduction",
                        new PodcastFragment("Willem Meints", "Welcome to this episode!"),
                        new PodcastFragment("Joop Snijder", "Hey, welcome!")),
                new PodcastSection("Discussion",
                        new PodcastFragment("Joop Snijder", "We're discussing stuff"),
                        new PodcastFragment("Willem Meints", "Ah, great, I love a discussion")),
                new PodcastSection("Conclusion",
                        new PodcastFragment("Willem Meints", "That's it!"),
                        new PodcastFragment("Joop Snijder", "Thanks for listening!"))
        );
    }
}
