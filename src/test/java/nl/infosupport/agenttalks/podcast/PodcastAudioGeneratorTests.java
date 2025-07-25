package nl.infosupport.agenttalks.podcast;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import nl.infosupport.agenttalks.podcast.elevenlabs.CreateSpeechRequest;
import nl.infosupport.agenttalks.podcast.elevenlabs.ElevenLabsClient;
import nl.infosupport.agenttalks.shared.TestObjectFactory;

@QuarkusTest
public class PodcastAudioGeneratorTests {
    @InjectMock
    @RestClient
    ElevenLabsClient elevenLabsClient;

    @Inject
    PodcastAudioGenerator podcastAudioGenerator;

    @InjectMock
    AudioConcatenation audioConcatenation;

    @Test
    public void testCanGeneratePodcastAudio() throws Exception {
        var sampleFileStream = new FileInputStream(
                new File("src/test/resources/audio/joop-fragment-01.mp3"));

        when(audioConcatenation.concatenateAudioFiles(any()))
                .thenReturn("src/test/resources/audio/joop-fragment-01.mp3");

        when(elevenLabsClient.createSpeech(anyString(), anyString(), any(CreateSpeechRequest.class)))
                .thenReturn(Response.ok().entity(sampleFileStream).build());

        var podcastScript = TestObjectFactory.generatePodcastScript();
        var audioFilePath = podcastAudioGenerator.generatePodcastAudio(podcastScript);

        assertNotNull(audioFilePath);
    }
}
