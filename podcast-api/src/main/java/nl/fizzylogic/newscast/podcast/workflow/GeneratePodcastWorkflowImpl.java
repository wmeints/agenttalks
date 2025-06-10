package nl.fizzylogic.newscast.podcast.workflow;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.model.PodcastScript;

public class GeneratePodcastWorkflowImpl implements GeneratePodcastWorkflow {
    private final ContentMetadataActivities contentMetadataActivities;
    private final GeneratePodcastScriptActivities podcastScriptGenerationActivities;
    private final GeneratePodcastAudioActivities generatePodcastAudioActivities;

    public GeneratePodcastWorkflowImpl() {
        var contentMetadataActivityOptions = ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(30)).build();

        var podcastScriptGenerationRetryOptions = RetryOptions.newBuilder()
                .setMaximumAttempts(5)
                .setInitialInterval(Duration.ofSeconds(30))
                .build();

        var podcastAudioGenerationRetryOptions = RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .setInitialInterval(Duration.ofMinutes(1))
                .build();

        var podcastScriptGenerationActivityOptions = ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofMinutes(5))
                .setRetryOptions(podcastScriptGenerationRetryOptions)
                .build();

        var podcastAudioGenerationActivityOptions = ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofMinutes(30))
                .setRetryOptions(podcastAudioGenerationRetryOptions)
                .build();

        contentMetadataActivities = Workflow.newActivityStub(
                ContentMetadataActivities.class,
                contentMetadataActivityOptions);

        podcastScriptGenerationActivities = Workflow.newActivityStub(
                GeneratePodcastScriptActivities.class,
                podcastScriptGenerationActivityOptions);

        generatePodcastAudioActivities = Workflow.newActivityStub(
                GeneratePodcastAudioActivities.class,
                podcastAudioGenerationActivityOptions);
    }

    @Override
    public void generatePodcast(GeneratePodcastWorkflowInput input) {
        // Lock the content submissions to prevent other workflows from picking them up.
        contentMetadataActivities.lockContentSubmissions(input.contentSubmissions);

        // Use Azure OpenAI to generate a podcast script based on the content
        // submissions. We'll use configured podcast host metadata for the style and
        // language patterns.
        PodcastScript script = podcastScriptGenerationActivities
                .generatePodcastScript(new GeneratePodcastScriptInput(input.contentSubmissions));

        // Collect all fragments from the script, skippping the intro and outro
        // sections. We'll use fixed audio fragments for the intro and outro.
        var processableFragments = script.sections.stream()
                // .skip(1).limit(script.sections.size() - 2)
                .flatMap(section -> section.fragments.stream())
                .collect(Collectors.toList());

        // Use elevenlabs to convert the text to speech for each fragment.
        // This will use the voice clone registered for each host.
        var generatedFragments = processableFragments.stream()
                .map(fragment -> generatePodcastAudioActivities.generateSpeech(fragment))
                .collect(Collectors.toList());

        // Concatenate the MP3 files into a single audio file.
        // We'll concatenate the intro, content, and outro sections in the next step.
        var contentAudioFile = generatePodcastAudioActivities.concatenateAudioFragments(generatedFragments);

        // Mix the intro, content, and outro sections into a single audio file.
        var finalAudioFile = generatePodcastAudioActivities.mixPodcastEpisode(contentAudioFile);

        // Save the podcast episode with the generated audio file and the included
        // content submissions.
        String showNotes = buildShowNotes(input.contentSubmissions);
        String description = buildDescription(input.contentSubmissions);
        
        contentMetadataActivities.savePodcastEpisode(
                script.title,
                finalAudioFile,
                showNotes,
                description,
                input.contentSubmissions);

        // Mark the content submissions as processed.
        // Other workflow triggers can't pick them up anymore.
        contentMetadataActivities.markContentSubmissionsAsProcessed(input.contentSubmissions);
    }

    private String buildShowNotes(List<ContentSubmission> contentSubmissions) {
        StringBuilder showNotes = new StringBuilder();
        showNotes.append("In this episode, we cover:\n\n");
        
        for (ContentSubmission submission : contentSubmissions) {
            if (submission.title != null && !submission.title.isEmpty()) {
                showNotes.append("• ").append(submission.title);
                if (submission.url != null && !submission.url.isEmpty()) {
                    showNotes.append(" (").append(submission.url).append(")");
                }
                showNotes.append("\n");
            }
        }
        
        return showNotes.toString();
    }

    private String buildDescription(List<ContentSubmission> contentSubmissions) {
        StringBuilder description = new StringBuilder();
        description.append("This episode covers the latest developments in technology and software engineering. ");
        description.append("We discuss ");
        
        if (!contentSubmissions.isEmpty()) {
            for (int i = 0; i < contentSubmissions.size(); i++) {
                ContentSubmission submission = contentSubmissions.get(i);
                if (submission.title != null && !submission.title.isEmpty()) {
                    if (i > 0 && i == contentSubmissions.size() - 1) {
                        description.append(" and ");
                    } else if (i > 0) {
                        description.append(", ");
                    }
                    description.append(submission.title.toLowerCase());
                }
            }
            description.append(".");
        }
        
        return description.toString();
    }

    @Override
    public PodcastGenerationStatus getStatus() {
        return new PodcastGenerationStatus();
    }
}
