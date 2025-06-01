package nl.fizzylogic.newscast.reader.processing;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.tika.TikaParser;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.reader.exceptions.ContentDownloadFailedException;
import nl.fizzylogic.newscast.reader.model.ContentDownload;
import nl.fizzylogic.newscast.reader.model.ContentSubmissionCreated;

@ApplicationScoped
public class ContentDownloader {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

    Logger logger = Logger.getLogger(ContentDownloader.class);

    @Inject
    TikaParser parser;

    @Incoming("content-downloader-input")
    @Outgoing("content-downloader-output")
    public JsonObject process(JsonObject message) {
        DatabindCodec.mapper().registerModule(new JavaTimeModule());

        ContentSubmissionCreated contentSubmissionCreated = message
                .mapTo(ContentSubmissionCreated.class);

        HttpClient httpClient = createHttpClient();

        logger.info(String.format("Downloading content for submission ID: %s from URL: %s",
                contentSubmissionCreated.contentSubmissionId, contentSubmissionCreated.url));

        try {
            HttpResponse response = executeDownloadRequest(contentSubmissionCreated, httpClient);
            var result = processDownloadResponse(contentSubmissionCreated, response);

            return JsonObject.mapFrom(result);
        } catch (IOException ex) {
            logger.error(String.format("Failed to download content from %s",
                    contentSubmissionCreated.url), ex);

            throw new ContentDownloadFailedException(
                    String.format("Failed to retrieve content from %s",
                            contentSubmissionCreated.url),
                    ex);
        } catch (ContentDownloadFailedException ex) {
            logger.error(String.format("Content download failed for %s: %s",
                    contentSubmissionCreated.url, ex.getMessage()), ex);

            throw ex;
        }
    }

    private ContentDownload processDownloadResponse(
            ContentSubmissionCreated contentSubmissionCreated,
            HttpResponse response) throws IOException {
        String contentType = response.getFirstHeader("Content-Type").getValue();
        InputStream contentStream = response.getEntity().getContent();
        String parsedContent = parser.parse(contentStream, contentType).getText();

        logger.info(String.format("Downloaded content from %s with content type: %s",
                contentSubmissionCreated.url, contentType));

        var result = new ContentDownload(
                contentSubmissionCreated.contentSubmissionId,
                contentType,
                parsedContent);

        return result;
    }

    private HttpResponse executeDownloadRequest(ContentSubmissionCreated contentSubmissionCreated,
            HttpClient httpClient)
            throws IOException, ClientProtocolException {
        HttpGet request = new HttpGet(contentSubmissionCreated.url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept", "*/*");

        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new ContentDownloadFailedException(
                    "Failed to download content from " + contentSubmissionCreated.url);
        }
        return response;
    }

    private HttpClient createHttpClient() {
        // We use a customized cookie configuration because some websites mess up
        // cookies pretty badly. This configuration is more lenient and should work with
        // most sites.
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }
}
