package nl.fizzylogic.newscast.reader.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.tika.TikaContent;
import io.quarkus.tika.TikaMetadata;
import io.quarkus.tika.TikaParser;
import io.vertx.core.json.JsonObject;
import nl.fizzylogic.newscast.reader.model.ContentDownload;
import nl.fizzylogic.newscast.reader.model.ContentSubmissionCreated;

@QuarkusTest
public class ContentDownloaderTest {
    @Test
    public void canDownloadContent() {
        var contentSubmissionCreated = new ContentSubmissionCreated(1L,
                "https://github.com/microsoft/vscode/issues/193543", LocalDateTime.now());

        var contentDownloader = new ContentDownloader();
        var tikaParser = mock(TikaParser.class);
        contentDownloader.parser = tikaParser;

        var parsedContent = new TikaContent("test", new TikaMetadata(new HashMap<>()));
        when(tikaParser.parse(any(), anyString())).thenReturn(parsedContent);

        var result = contentDownloader.process(JsonObject.mapFrom(contentSubmissionCreated));
        var resultObject = result.mapTo(ContentDownload.class);

        assertNotNull(result);
        assertEquals("test", resultObject.body);
    }
}
