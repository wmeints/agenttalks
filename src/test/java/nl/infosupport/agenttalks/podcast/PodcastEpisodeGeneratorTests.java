package nl.infosupport.agenttalks.podcast;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.podcast.buzzsprout.BuzzsproutClient;
import nl.infosupport.agenttalks.podcast.buzzsprout.CreateEpisodeRequest;
import nl.infosupport.agenttalks.shared.TestObjectFactory;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@QuarkusTest
public class PodcastEpisodeGeneratorTests {
    @InjectMock
    PodcastScriptGenerator podcastScriptGenerator;

    @InjectMock
    ShowNotesGenerator showNotesGenerator;

    @InjectMock
    EpisodeSummaryGenerator episodeSummaryGenerator;

    @InjectMock
    PodcastAudioGenerator podcastAudioGenerator;

    @Inject
    PodcastEpisodeGenerator podcastEpisodeGenerator;

    @RestClient
    @InjectMock
    BuzzsproutClient buzzsproutClient;

    @Test
    @TestTransaction
    public void testCanGeneratePodcastScript() {
        var submission1 = TestObjectFactory.generateContentSubmission();
        var submission2 = TestObjectFactory.generateContentSubmission();
        var submission3 = TestObjectFactory.generateContentSubmission();

        submission1.persistAndFlush();
        submission2.persistAndFlush();
        submission3.persistAndFlush();

        when(showNotesGenerator.generateShowNotes(anyString(), anyString()))
                .thenReturn("Show notes");

        when(episodeSummaryGenerator.generateEpisodeDescription(anyString(), anyString()))
                .thenReturn("Episode summary");

        when(podcastScriptGenerator.generatePodcastScript(anyString(), anyString(), anyString(), anyString(),
                anyString()))
                .thenReturn(TestObjectFactory.generatePodcastScript());

        when(podcastAudioGenerator.generatePodcastAudio(any(PodcastScript.class))).thenReturn("test.mp3");

        podcastEpisodeGenerator.generatePodcastEpisode();

        verify(podcastAudioGenerator).generatePodcastAudio(any(PodcastScript.class));
        verify(buzzsproutClient).createEpisode(anyString(), any(CreateEpisodeRequest.class));
    }
}
