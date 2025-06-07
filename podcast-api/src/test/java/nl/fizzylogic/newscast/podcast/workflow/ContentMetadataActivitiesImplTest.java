package nl.fizzylogic.newscast.podcast.workflow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

import nl.fizzylogic.newscast.podcast.clients.content.ContentClient;
import nl.fizzylogic.newscast.podcast.clients.content.model.CreatePodcastEpisode;
import nl.fizzylogic.newscast.podcast.shared.TestObjectFactory;

class ContentMetadataActivitiesImplTest {

    private ContentMetadataActivitiesImpl activities;
    private ContentClient contentClient;
    private BlobServiceClient blobServiceClient;
    private BlobContainerClient blobContainerClient;
    private BlobClient blobClient;

    @BeforeEach
    void setUp() {
        contentClient = mock(ContentClient.class);
        blobServiceClient = mock(BlobServiceClient.class);
        blobContainerClient = mock(BlobContainerClient.class);
        blobClient = mock(BlobClient.class);

        activities = new ContentMetadataActivitiesImpl();
        activities.contentClient = contentClient;
        activities.blobServiceClient = blobServiceClient;

        when(blobServiceClient.getBlobContainerClient(anyString())).thenReturn(blobContainerClient);
        when(blobContainerClient.getBlobClient(anyString())).thenReturn(blobClient);
    }

    @Test
    void testLockContentSubmissions() {
        var submissions = List.of(
                TestObjectFactory.createSummarizedSubmission(),
                TestObjectFactory.createSummarizedSubmission());

        activities.lockContentSubmissions(submissions);

        verify(contentClient, times(2)).markForProcessing(any());
    }

    @Test
    void testMarkContentSubmissionsAsProcessed() {
        var submissions = List.of(
                TestObjectFactory.createSummarizedSubmission(),
                TestObjectFactory.createSummarizedSubmission());

        activities.markContentSubmissionsAsProcessed(submissions);

        verify(contentClient, times(2)).markAsProcessed(any());
    }

    @Test
    void testSavePodcastEpisode() throws Exception {
        // Create a temp file to simulate audio file
        File tempFile = File.createTempFile("test-audio", ".mp3");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("audio".getBytes());
        }

        when(blobClient.getBlobName()).thenReturn(tempFile.getName());

        activities.savePodcastEpisode("Test Title", tempFile.getAbsolutePath(), List.of());

        verify(blobContainerClient).createIfNotExists();
        verify(blobContainerClient).getBlobClient(tempFile.getName());
        verify(blobClient).upload(any(FileInputStream.class));
        verify(contentClient).createPodcastEpisode(any(CreatePodcastEpisode.class));

        tempFile.delete();
    }
}
