package nl.infosupport.agenttalks.podcast.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.faulttolerance.Retry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.podcast.clients.elevenlabs.ElevenLabsClient;
import nl.infosupport.agenttalks.podcast.clients.elevenlabs.model.CreateSpeechRequest;
import nl.infosupport.agenttalks.podcast.model.PodcastFragment;
import nl.infosupport.agenttalks.podcast.model.PodcastHost;

@ApplicationScoped
public class AudioProcessor {
    
    @Inject
    @RestClient
    ElevenLabsClient elevenLabsClient;

    @ConfigProperty(name = "newscast.locations.audio-files")
    String outputDirectoryPath;

    @Inject
    AudioConcatenation audioConcatenation;
    
    private static final Logger logger = Logger.getLogger(AudioProcessor.class);

    @Transactional
    @Retry(maxRetries = 3)
    public String generateSpeech(PodcastFragment fragment) {
        logger.infof("Generating speech for fragment from host: %s", fragment.host);
        
        var podcastHost = PodcastHost.findByName(fragment.host);

        if (podcastHost == null) {
            throw new RuntimeException(String.format("Podcast host not found: %s", fragment.host));
        }

        var response = elevenLabsClient.createSpeech(
                podcastHost.voiceId,
                "mp3_44100_128",
                new CreateSpeechRequest(fragment.content));

        // Write response content to a file in the data directory
        File dataDir = new File(outputDirectoryPath);

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        String filename = String.format("%s.mp3", UUID.randomUUID());
        File outputFile = new File(dataDir, filename);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            response.readEntity(InputStream.class).transferTo(fos);
        } catch (IOException e) {
            logger.errorf("Failed to write audio file: %s", outputFile.getPath());
            throw new RuntimeException("Failed to write audio file", e);
        }

        logger.infof("Generated speech file: %s", outputFile.getPath());
        return outputFile.getPath();
    }

    public String concatenateAudioFragments(List<String> audioFragments) {
        logger.infof("Concatenating %d audio fragments", audioFragments.size());
        
        // NOTE: The assumption here is that the workflow is always running on one
        // machine. If this is ever changed, we need to ensure that we download the audio
        // fragments from blob storage.

        String filename = String.format("%s.mp3", UUID.randomUUID());
        File dataDir = new File(outputDirectoryPath);
        File outputFile = new File(dataDir, filename);

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try {
            audioConcatenation.concatenateAudioFiles(audioFragments, outputFile);
        } catch (Exception e) {
            logger.errorf("Failed to concatenate audio fragments: %s", e.getMessage());
            throw new RuntimeException("Failed to concatenate audio fragments", e);
        }

        logger.infof("Concatenated audio file: %s", outputFile.getPath());
        return outputFile.getPath();
    }

    public String mixPodcastEpisode(String contentAudioFile) {
        logger.infof("Mixing podcast episode with content audio: %s", contentAudioFile);
        
        // TODO: Implement mixing logic with intro/outro
        // For now, just return the content audio file
        logger.warn("Podcast episode mixing not yet implemented, returning content audio file as-is");
        return contentAudioFile;
    }
}