package nl.infosupport.agenttalks.podcast;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.podcast.elevenlabs.CreateSpeechRequest;
import nl.infosupport.agenttalks.podcast.elevenlabs.ElevenLabsClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.azure.storage.blob.BlobServiceClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PodcastAudioGenerator {
    @RestClient
    ElevenLabsClient elevenLabsClient;

    @ConfigProperty(name = "agenttalks.locations.audio-files")
    String outputDirectoryPath;

    @Inject
    AudioConcatenation audioConcatenation;

    @Inject
    BlobServiceClient blobStorageClient;

    private static final Logger logger = Logger.getLogger(PodcastAudioGenerator.class);

    private static final String FORMAT = "mp3_44100_128";

    public String generatePodcastAudio(PodcastScript script) {
        var fragments = script.sections.stream()
                .flatMap(s -> s.fragments.stream())
                .map(this::generateSpeechFragment)
                .toList();

        var audioFilePath = concatenateAudioFragments(fragments);

        return uploadAudioFile(audioFilePath);
    }

    private String generateSpeechFragment(PodcastFragment fragment) {
        var host = PodcastHost.findByName(fragment.host);
        var createSpeechRequest = new CreateSpeechRequest(fragment.content);
        try (var response = elevenLabsClient.createSpeech(host.voiceId, FORMAT, createSpeechRequest)) {
            File dataDir = new File(outputDirectoryPath);

            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            String filename = String.format("%s.mp3", UUID.randomUUID());
            File outputFile = new File(dataDir, filename);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                response.readEntity(InputStream.class).transferTo(fos);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write audio file", e);
            }

            return outputFile.getPath();
        }
    }

    private String uploadAudioFile(String filePath) {
        File audioFile = new File(filePath);

        var blobContainerClient = blobStorageClient.getBlobContainerClient("episodes");
        var blobClient = blobContainerClient.getBlobClient(audioFile.getName());

        if (blobContainerClient.createIfNotExists()) {
            logger.warn(
                    "Blob container 'episodes' automatically created. You may need to configure " +
                            "public access so buzzsprout can access the files.");
        }

        blobClient.uploadFromFile(filePath, true);

        return blobClient.getBlobUrl();
    }

    private String concatenateAudioFragments(List<String> audioFragments) {
        try {
            return audioConcatenation.concatenateAudioFiles(audioFragments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to concatenate audio fragments", e);
        }
    }
}
