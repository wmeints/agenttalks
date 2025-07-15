package nl.infosupport.agenttalks.summarization;

import java.io.IOException;
import java.io.InputStream;

import io.quarkus.vertx.ConsumeEvent;
import nl.infosupport.agenttalks.submissions.ContentSubmission;
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

import io.quarkus.tika.TikaParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContentProcessor {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

    private final Logger logger = Logger.getLogger(ContentProcessor.class);

    @Inject
    TikaParser documentParser;

    @Inject
    ContentSummarizer summarizerAgent;

    @ConsumeEvent("submissions.created")
    public void processContentSubmission(ContentSubmission contentSubmission) {
        try {
            // Step 1: Download and parse content
            ContentSummarizationData contentData = downloadContent(contentSubmission);

            // Step 2: Summarize content using AI
            ContentSummarizationData summarizedData = summarizeContent(contentData);

            // Step 3: Update content submission
            contentSubmission.summarize(summarizedData.title, summarizedData.summary);
            contentSubmission.persist();
        } catch (Exception ex) {
            logger.errorf("Failed to process content submission ID: %s - %s",
                    contentSubmission.id, ex.getMessage());

            throw new RuntimeException("Content processing failed", ex);
        }
    }

    @Retry(maxRetries = 3, delay = 1000, jitter = 200)
    @Timeout(value = 30000) // 30 seconds timeout
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000)
    ContentSummarizationData downloadContent(ContentSubmission contentSubmission)
            throws IOException {
        HttpClient httpClient = createHttpClient();

        try {
            HttpResponse response = executeDownloadRequest(contentSubmission.url, httpClient);
            return processDownloadResponse(contentSubmission, response);
        } catch (IOException ex) {
            throw new ContentDownloadFailedException(
                    String.format("Failed to retrieve content from %s", contentSubmission.url), ex);
        }
    }

    @Retry(maxRetries = 5, delay = 2000, jitter = 500)
    @Timeout(value = 120_000) // 60-second timeout for AI processing
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.6, delay = 10000)
    ContentSummarizationData summarizeContent(ContentSummarizationData contentData) {
        var contentSummary = summarizerAgent.summarizeContent(contentData.body);
        ContentSummarizationData result = contentData.withSummary(contentSummary.title, contentSummary.summary);
        return result;
    }

    private HttpResponse executeDownloadRequest(String url,
            HttpClient httpClient) throws IOException, ClientProtocolException {
        HttpGet request = new HttpGet(url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "*/*");

        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new ContentDownloadFailedException(
                    "Failed to download content from " + url
                            + " - HTTP " + response.getStatusLine().getStatusCode());
        }
        return response;
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