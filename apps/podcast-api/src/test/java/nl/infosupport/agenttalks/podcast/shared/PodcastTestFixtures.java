package nl.infosupport.agenttalks.podcast.shared;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.mockito.Mockito;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.BuzzsproutClient;
import nl.infosupport.agenttalks.podcast.clients.buzzsprout.model.CreateEpisodeResponse;
import nl.infosupport.agenttalks.podcast.clients.content.ContentClient;
import nl.infosupport.agenttalks.podcast.clients.content.model.CreatePodcastEpisode;
import nl.infosupport.agenttalks.podcast.clients.elevenlabs.ElevenLabsClient;
import nl.infosupport.agenttalks.podcast.service.AudioConcatenation;
import nl.infosupport.agenttalks.podcast.service.PodcastScriptGenerator;

public class PodcastTestFixtures {

    public static class MockSetup {
        public ContentClient contentClient;
        public BlobServiceClient blobServiceClient;
        public BlobContainerClient blobContainerClient;
        public BlobClient blobClient;
        public PodcastScriptGenerator scriptGenerator;
        public ElevenLabsClient elevenLabsClient;
        public AudioConcatenation audioConcatenation;
        public BuzzsproutClient buzzsproutClient;

        public MockSetup() {
            contentClient = Mockito.mock(ContentClient.class);
            blobServiceClient = Mockito.mock(BlobServiceClient.class);
            blobContainerClient = Mockito.mock(BlobContainerClient.class);
            blobClient = Mockito.mock(BlobClient.class);
            scriptGenerator = Mockito.mock(PodcastScriptGenerator.class);
            elevenLabsClient = Mockito.mock(ElevenLabsClient.class);
            audioConcatenation = Mockito.mock(AudioConcatenation.class);
            buzzsproutClient = Mockito.mock(BuzzsproutClient.class);

            setupDefaultBehaviors();
        }

        private void setupDefaultBehaviors() {
            // Azure Blob Storage mocks
            when(blobServiceClient.getBlobContainerClient(anyString())).thenReturn(blobContainerClient);
            when(blobContainerClient.getBlobClient(anyString())).thenReturn(blobClient);
            when(blobClient.getBlobName()).thenReturn("test-episode.mp3");

            // Content API mocks
            when(contentClient.createPodcastEpisode(any(CreatePodcastEpisode.class)))
                    .thenReturn(ErrorOr.of(TestObjectFactory.createPodcastEpisode()));

            // Buzzsprout mocks
            CreateEpisodeResponse buzzsproutResponse = new CreateEpisodeResponse();
            buzzsproutResponse.id = 12345L;
            buzzsproutResponse.title = "Test Episode";
            buzzsproutResponse.publicUrl = "https://buzzsprout.com/test/12345";

            when(buzzsproutClient.createEpisode(anyString(), any())).thenReturn(buzzsproutResponse);
        }
    }

    public static MockSetup createMockSetup() {
        return new MockSetup();
    }

    public static List<String> createAudioFragmentPaths() {
        return List.of(
                "/path/to/fragment1.mp3",
                "/path/to/fragment2.mp3");
    }

    public static String createFinalAudioPath() {
        return "/path/to/final-episode.mp3";
    }
}