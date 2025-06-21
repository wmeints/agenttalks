package nl.fizzylogic.newscast.podcast.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.azure.storage.blob.BlobServiceClient;

import jakarta.inject.Inject;
import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.CreatePodcastEpisode;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkAsProcessed;
import nl.fizzylogic.newscast.podcast.clients.content.model.MarkForProcessing;

public class ContentMetadataActivitiesImpl implements ContentMetadataActivities {
    @Inject
    ContentClient contentClient;

    @Inject
    BlobServiceClient blobServiceClient;

    @Override
    public void lockContentSubmissions(List<ContentSubmission> contentSubmissions) {
        for (var contentSubmission : contentSubmissions) {
            contentClient.markForProcessing(new MarkForProcessing(contentSubmission.id));
        }
    }

    @Override
    public void markContentSubmissionsAsProcessed(List<ContentSubmission> contentSubmissions) {
        for (var contentSubmission : contentSubmissions) {
            contentClient.markAsProcessed(new MarkAsProcessed(contentSubmission.id));
        }
    }

    @Override
    public String savePodcastEpisode(String title, String audioFilePath, String showNotes, String description,
            List<ContentSubmission> contentSubmissions) {
        var audioFile = new File(audioFilePath);
        var episodesContainer = blobServiceClient.getBlobContainerClient("episodes");

        episodesContainer.createIfNotExists();

        try (var inputStream = new FileInputStream(audioFile)) {
            var blob = episodesContainer.getBlobClient(audioFile.getName());
            blob.upload(inputStream);

            contentClient.createPodcastEpisode(new CreatePodcastEpisode(blob.getBlobName(), title, showNotes, description));
            
            return blob.getBlobUrl();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Audio file not found: %s", audioFilePath), e);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error uploading audio file: %s", audioFilePath), e);
        }
    }
}
