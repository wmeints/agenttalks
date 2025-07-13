package nl.infosupport.agenttalks.podcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import nl.infosupport.agenttalks.podcast.clients.elevenlabs.model.CreateSpeechRequest;
import nl.infosupport.agenttalks.podcast.model.PodcastFragment;
import nl.infosupport.agenttalks.podcast.model.PodcastHost;
import nl.infosupport.agenttalks.podcast.shared.PodcastTestFixtures;

@QuarkusTest
class AudioProcessorTest {

    private AudioProcessor audioProcessor;
    private PodcastTestFixtures.MockSetup mocks;

    @TempDir
    File tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        mocks = PodcastTestFixtures.createMockSetup();
        tempDir = Files.createTempDirectory("audio_test_").toFile();

        audioProcessor = new AudioProcessor();
        audioProcessor.elevenLabsClient = mocks.elevenLabsClient;
        audioProcessor.audioConcatenation = mocks.audioConcatenation;
        audioProcessor.outputDirectoryPath = tempDir.getAbsolutePath();

        PanacheMock.mock(PodcastHost.class);
    }

    @Test
    void generateSpeech_happyFlow_writesFileAndReturnsPath() throws Exception {
        // Arrange
        var audioFragmentStream = new FileInputStream("src/test/resources/audio/joop-fragment-01.mp3");
        var fakeResponse = Response.ok().entity(audioFragmentStream).build();

        when(mocks.elevenLabsClient.createSpeech(anyString(), anyString(), any(CreateSpeechRequest.class)))
                .thenReturn(fakeResponse);

        PodcastFragment fragment = new PodcastFragment();
        fragment.host = "Joop Snijder";
        fragment.content = "Hello world!";

        when(PodcastHost.findByName("Joop Snijder")).thenReturn(
                new PodcastHost("Joop Snijder", "", "", "test-123", 1));

        // Act
        String path = audioProcessor.generateSpeech(fragment);

        // Assert
        assertTrue(path.endsWith(".mp3"));
        assertTrue(Files.exists(Paths.get(path)));
        assertTrue(Files.size(Paths.get(path)) > 0);
    }

    @Test
    void generateSpeech_hostNotFound_throwsException() {
        // Arrange
        PodcastFragment fragment = new PodcastFragment();
        fragment.host = "Unknown Host";
        fragment.content = "Hello world!";

        when(PodcastHost.findByName("Unknown Host")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> audioProcessor.generateSpeech(fragment));

        assertEquals("Podcast host not found: Unknown Host", exception.getMessage());
    }

    @Test
    void concatenateAudioFragments_happyFlow_createsOutputFile() {
        // Arrange
        List<String> audioFragments = List.of(
                new File("src/test/resources/audio/joop-fragment-01.mp3").getAbsolutePath(),
                new File("src/test/resources/audio/willem-fragment-01.mp3").getAbsolutePath());

        // Act
        String path = audioProcessor.concatenateAudioFragments(audioFragments);

        // Assert
        assertTrue(path.endsWith(".mp3"));
    }

    @Test
    void mixPodcastEpisode_returnsContentAudioFileAsIs() {
        // Arrange
        String contentAudioFile = "/test/path/content.mp3";
        
        // Act
        String result = audioProcessor.mixPodcastEpisode(contentAudioFile);
        
        // Assert
        assertEquals(contentAudioFile, result);
    }
}