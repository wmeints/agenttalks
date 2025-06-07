package nl.fizzylogic.newscast.podcast.processing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.fizzylogic.newscast.podcast.clients.content.model.ContentSubmission;
import nl.fizzylogic.newscast.podcast.clients.content.model.SubmissionStatus;
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
    @Timeout(120000) // Increase timeout for LLM processing
    public void canGeneratePodcastScript() {
        PodcastHost.builder().withIndex(0).withName("Host One")
                .withStyleInstructions("Friendly and engaging").build().persist();

        PodcastHost.builder().withIndex(1).withName("Host Two")
                .withStyleInstructions("Friendly and engaging").build().persist();

        var podcastData = PodcastEpisodeData.builder().withStartDate(LocalDate.now())
                .withEndDate(LocalDate.now().plusDays(1))
                .withContentSubmissions(List.of(sampleContentSubmission()))
                .build();

        when(podcastScriptGenerator.generatePodcastScript(anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(new PodcastScript());

        var response = podcastScriptGeneratorStep.process(podcastData);
    }

    private ContentSubmission sampleContentSubmission() {
        String title = "Analysis of Explainers of Black Box Deep Neural Networks for Computer Vision: A Survey";
        String summary = """
                Deep Learning is a state-of-the-art technique to make inference on extensive or complex data.
                As a black box model due to their multilayer nonlinear structure, Deep Neural Networks are often
                criticized to be non-transparent and their predictions not traceable by humans. Furthermore, the models
                learn from artificial datasets, often with bias or contaminated discriminating content. Through their
                increased distribution, decision-making algorithms can contribute promoting prejudge and unfairness
                which is not easy to notice due to lack of transparency. Hence, scientists developed several so-called
                explanators or explainers which try to point out the connection between input and output to represent
                in a simplified way the inner structure of machine learning black boxes. In this survey we differ the
                mechanisms and properties of explaining systems for Deep Neural Networks for Computer Vision tasks.
                We give a comprehensive overview about taxonomy of related studies and compare several survey
                papers that deal with explainability in general. We work out the drawbacks and gaps and summarize
                further research ideas""";

        return new ContentSubmission(1L, title, summary, SubmissionStatus.SUMMARIZED,
                "https://arxiv.org/pdf/1911.12116");
    }
}
