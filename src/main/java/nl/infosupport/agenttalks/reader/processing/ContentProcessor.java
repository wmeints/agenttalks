package nl.infosupport.agenttalks.reader.processing;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.tika.TikaParser;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.content.service.ContentService;
import nl.infosupport.agenttalks.reader.exceptions.ContentDownloadFailedException;
import nl.infosupport.agenttalks.reader.model.ContentSubmissionCreated;
import nl.infosupport.agenttalks.reader.model.ContentSummarizationData;
import nl.infosupport.agenttalks.reader.service.ContentSummarizer;

@ApplicationScoped
public class ContentProcessor {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

    private final Logger logger = Logger.getLogger(ContentProcessor.class);

    @Inject
    TikaParser documentParser;

    @Inject
    ContentSummarizer summarizerAgent;

    @Inject
    ContentService contentService;

    @ConsumeEvent(value = "content.submission.created", blocking = true)
    public Uni<Void> processContentSubmission(JsonObject message) {
        return Uni.createFrom().item(() -> {
            DatabindCodec.mapper().registerModule(new JavaTimeModule());

            ContentSubmissionCreated contentSubmissionCreated = message
                    .mapTo(ContentSubmissionCreated.class);

            logger.infof("Starting content processing for submission ID: %s from URL: %s",
                    contentSubmissionCreated.contentSubmissionId, contentSubmissionCreated.url);

            try {
                // Step 1: Download and parse content
                ContentSummarizationData contentData = downloadContent(contentSubmissionCreated);

                // Step 2: Summarize content using AI
                ContentSummarizationData summarizedData = summarizeContent(contentData);

                // Step 3: Update content submission
                updateContentSubmission(summarizedData);

                logger.infof("Successfully processed content submission ID: %s",
                        contentSubmissionCreated.contentSubmissionId);

                return null;
            } catch (Exception ex) {
                logger.errorf("Failed to process content submission ID: %s - %s",
                        contentSubmissionCreated.contentSubmissionId, ex.getMessage());
                throw new RuntimeException("Content processing failed", ex);
            }
        });
    }

    @Retry(maxRetries = 3, delay = 1000, jitter = 200)
    @Timeout(value = 30000) // 30 seconds timeout
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000)
    ContentSummarizationData downloadContent(ContentSubmissionCreated contentSubmissionCreated)
            throws IOException {
        logger.infof("Downloading content for submission ID: %s from URL: %s",
                contentSubmissionCreated.contentSubmissionId, contentSubmissionCreated.url);

        HttpClient httpClient = createHttpClient();

        try {
            HttpResponse response = executeDownloadRequest(contentSubmissionCreated, httpClient);
            ContentSummarizationData result = processDownloadResponse(contentSubmissionCreated, response);

            logger.infof("Successfully downloaded content for submission ID: %s",
                    contentSubmissionCreated.contentSubmissionId);
            return result;

        } catch (IOException ex) {
            logger.errorf("Failed to download content from %s: %s",
                    contentSubmissionCreated.url, ex.getMessage());

            throw new ContentDownloadFailedException(
                    String.format("Failed to retrieve content from %s", contentSubmissionCreated.url), ex);
        }
    }

    @Retry(maxRetries = 5, delay = 2000, jitter = 500)
    @Timeout(value = 60000) // 60 seconds timeout for AI processing
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.6, delay = 10000)
    ContentSummarizationData summarizeContent(ContentSummarizationData contentData) {
        logger.infof("Summarizing content for submission ID: %s", contentData.contentSubmissionId);

        var contentSummary = summarizerAgent.summarizeContent(contentData.body);
        ContentSummarizationData result = contentData.withSummary(contentSummary.title, contentSummary.summary);

        logger.infof("Successfully summarized content for submission ID: %s", contentData.contentSubmissionId);
        return result;
    }

    @Retry(maxRetries = 3, delay = 1000, jitter = 300)
    @Timeout(value = 15000) // 15 seconds timeout for service calls
    @CircuitBreaker(requestVolumeThreshold = 8, failureRatio = 0.4, delay = 5000)
    void updateContentSubmission(ContentSummarizationData data) {
        logger.infof("Updating content submission with ID: %d", data.contentSubmissionId);

        try {
            contentService.summarizeContent(data.contentSubmissionId, data.title, data.summary);

            logger.infof("Successfully updated content submission ID: %d", data.contentSubmissionId);

        } catch (Exception ex) {
            logger.errorf("Failed to update content submission ID: %d - %s",
                    data.contentSubmissionId, ex.getMessage());
            throw ex;
        }
    }

    private HttpResponse executeDownloadRequest(ContentSubmissionCreated contentSubmissionCreated,
            HttpClient httpClient) throws IOException, ClientProtocolException {
        HttpGet request = new HttpGet(contentSubmissionCreated.url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "*/*");

        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new ContentDownloadFailedException(
                    "Failed to download content from " + contentSubmissionCreated.url
                            + " - HTTP " + response.getStatusLine().getStatusCode());
        }
        return response;
    }

    private ContentSummarizationData processDownloadResponse(
            ContentSubmissionCreated contentSubmissionCreated,
            HttpResponse response) throws IOException {
        String contentType = response.getFirstHeader("Content-Type").getValue();
        InputStream contentStream = response.getEntity().getContent();
        String parsedContent = documentParser.parse(contentStream, contentType).getText();

        logger.infof("Downloaded content from %s with content type: %s",
                contentSubmissionCreated.url, contentType);

        return new ContentSummarizationData(
                contentSubmissionCreated.contentSubmissionId,
                contentSubmissionCreated.url,
                parsedContent,
                contentType);
    }

    private HttpClient createHttpClient() {
        // We use a customized cookie configuration because some websites mess up
        // cookies pretty badly. This configuration is more lenient and should work with
        // most sites.
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }
}