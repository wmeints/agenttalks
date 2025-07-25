package nl.infosupport.agenttalks.reader;

import io.quarkus.tika.TikaParser;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.infosupport.agenttalks.content.ContentSubmission;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class ContentProcessor {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

    private final Logger logger = Logger.getLogger(ContentProcessor.class);

    @Inject
    TikaParser documentParser;

    @Inject
    ContentSummarizer summarizerAgent;

    @ConsumeEvent(value = "submissions.created", blocking = true)
    public void processContentSubmission(ContentSubmission contentSubmission) {
        logger.infof("Starting content processing for submission ID: %s from URL: %s",
                contentSubmission.id, contentSubmission.url);

        try {
            // Step 1: Download and parse content
            ContentSummarizationData contentData = downloadContent(contentSubmission);

            // Step 2: Summarize content using AI
            ContentSummarizationData summarizedData = summarizeContent(contentData);

            // Step 3: Update content submission
            updateContentSubmission(summarizedData);

            logger.infof("Successfully processed content submission ID: %s",
                    contentSubmission.id);
        } catch (Exception ex) {
            logger.errorf("Failed to process content submission ID: %s - %s",
                    contentSubmission.id, ex.getMessage());
            throw new RuntimeException("Content processing failed", ex);
        }
    }

    @Retry(maxRetries = 3, delay = 1000, jitter = 200)
    @Timeout(value = 30000) // 30-seconds timeout
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000)
    ContentSummarizationData downloadContent(ContentSubmission contentSubmission) throws IOException {
        logger.infof("Downloading content for submission ID: %s from URL: %s",
                contentSubmission.id, contentSubmission.url);

        HttpClient httpClient = createHttpClient();

        try {
            HttpResponse response = executeDownloadRequest(contentSubmission, httpClient);
            ContentSummarizationData result = processDownloadResponse(contentSubmission, response);

            logger.infof("Successfully downloaded content for submission ID: %s",
                    contentSubmission.id);
            return result;

        } catch (IOException ex) {
            logger.errorf("Failed to download content from %s: %s",
                    contentSubmission.url, ex.getMessage());

            throw new ContentDownloadFailedException(
                    String.format("Failed to retrieve content from %s", contentSubmission.url), ex);
        }
    }

    @Retry(maxRetries = 5, delay = 2000, jitter = 500)
    @Timeout(value = 60000) // 60-seconds timeout for AI processing
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.6, delay = 10000)
    ContentSummarizationData summarizeContent(ContentSummarizationData contentData) {
        logger.infof("Summarizing content for submission ID: %s", contentData.contentSubmissionId);

        var contentSummary = summarizerAgent.summarizeContent(contentData.body);
        ContentSummarizationData result = contentData.withSummary(contentSummary.title, contentSummary.summary);

        logger.infof("Successfully summarized content for submission ID: %s", contentData.contentSubmissionId);
        return result;
    }

    @Transactional
    void updateContentSubmission(ContentSummarizationData data) {
        logger.infof("Updating content submission with ID: %d", data.contentSubmissionId);

        try {
            ContentSubmission submission = ContentSubmission.findById(data.contentSubmissionId);

            submission.summarize(data.title, data.summary);
            submission.persistAndFlush();

            logger.infof("Successfully updated content submission ID: %d", data.contentSubmissionId);
        } catch (Exception ex) {
            logger.errorf("Failed to update content submission ID: %d - %s",
                    data.contentSubmissionId, ex.getMessage());
            throw ex;
        }
    }

    private HttpResponse executeDownloadRequest(ContentSubmission contentSubmission, HttpClient httpClient) {
        HttpGet request = new HttpGet(contentSubmission.url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "*/*");

        try {
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() != 200) {
                var exceptionMessage = String.format(
                        "Failed to download content from %s - HTTP %d",
                        contentSubmission.url,
                        response.getStatusLine().getStatusCode());

                throw new ContentDownloadFailedException(
                        exceptionMessage);
            }

            return response;
        } catch (IOException ex) {
            throw new ContentDownloadFailedException(
                    String.format("Failed to download content from %s", contentSubmission.url), ex);
        }
    }

    private ContentSummarizationData processDownloadResponse(
            ContentSubmission contentSubmission,
            HttpResponse response) throws IOException {
        String contentType = response.getFirstHeader("Content-Type").getValue();
        InputStream contentStream = response.getEntity().getContent();
        String parsedContent = documentParser.parse(contentStream, contentType).getText();

        logger.infof("Downloaded content from %s with content type: %s",
                contentSubmission.url, contentType);

        return new ContentSummarizationData(
                contentSubmission.id,
                contentSubmission.url,
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