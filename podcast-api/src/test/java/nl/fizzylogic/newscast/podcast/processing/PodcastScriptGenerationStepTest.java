package nl.fizzylogic.newscast.podcast.processing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.model.PodcastEpisodeData;
import nl.fizzylogic.newscast.podcast.model.PodcastHost;
import nl.fizzylogic.newscast.podcast.model.PodcastScript;
import nl.fizzylogic.newscast.podcast.service.PodcastScriptGenerator;

@QuarkusTest
public class PodcastScriptGenerationStepTest {
    @Inject
    PodcastScriptGenerationStep podcastScriptGeneratorStep;

    @InjectMock
    PodcastScriptGenerator podcastScriptGenerator;

    @Test
    @Transactional
    public void canGeneratePodcastScript() {
        PodcastHost.builder().withIndex(0).withName("Host One")
                .withStyleInstructions("Friendly and engaging").build().persist();

        PodcastHost.builder().withIndex(1).withName("Host Two")
                .withStyleInstructions("Friendly and engaging").build().persist();

        when(podcastScriptGenerator.generatePodcastScript(anyString(),
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new PodcastScript());

        var podcastData = PodcastEpisodeData.builder().withStartDate(LocalDate.now())
                .withEndDate(LocalDate.now().plusDays(1))
                .withContentSubmissions(List.of(new ContentSubmission()))
                .build();

        var response = podcastScriptGeneratorStep.process(podcastData);

        assertNotNull(response);
    }
}
