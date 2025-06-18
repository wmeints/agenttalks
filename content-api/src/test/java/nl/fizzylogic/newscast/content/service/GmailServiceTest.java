package nl.fizzylogic.newscast.content.service;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GmailServiceTest {

    @Test
    public void testGetSenderFromMessage() {
        GmailService gmailService = new GmailService();
        
        Message message = new Message();
        MessagePart payload = new MessagePart();
        MessagePartHeader fromHeader = new MessagePartHeader()
            .setName("From")
            .setValue("Joop Test <joop@example.com>");
        
        payload.setHeaders(Arrays.asList(fromHeader));
        message.setPayload(payload);

        Optional<String> sender = gmailService.getSender(message);
        
        assertTrue(sender.isPresent());
        assertEquals("joop@example.com", sender.get());
    }

    @Test
    public void testGetSenderFromMessageWithoutBrackets() {
        GmailService gmailService = new GmailService();
        
        Message message = new Message();
        MessagePart payload = new MessagePart();
        MessagePartHeader fromHeader = new MessagePartHeader()
            .setName("From")
            .setValue("joop@example.com");
        
        payload.setHeaders(Arrays.asList(fromHeader));
        message.setPayload(payload);

        Optional<String> sender = gmailService.getSender(message);
        
        assertTrue(sender.isPresent());
        assertEquals("joop@example.com", sender.get());
    }

    @Test
    public void testGetSenderFromMessageWithoutFromHeader() {
        GmailService gmailService = new GmailService();
        
        Message message = new Message();
        MessagePart payload = new MessagePart();
        MessagePartHeader subjectHeader = new MessagePartHeader()
            .setName("Subject")
            .setValue("Test Subject");
        
        payload.setHeaders(Arrays.asList(subjectHeader));
        message.setPayload(payload);

        Optional<String> sender = gmailService.getSender(message);
        
        assertFalse(sender.isPresent());
    }

    @Test
    public void testExtractUrlsFromMessage() {
        GmailService gmailService = new GmailService();
        
        Message message = new Message();
        MessagePart payload = new MessagePart();
        
        String content = "Check out this article: https://example.com/article and this one https://news.com/story";
        MessagePartBody body = new MessagePartBody();
        body.setData(java.util.Base64.getEncoder().encodeToString(content.getBytes()));
        
        payload.setBody(body);
        message.setPayload(payload);

        List<String> urls = gmailService.extractUrls(message);
        
        assertEquals(2, urls.size());
        assertTrue(urls.contains("https://example.com/article"));
        assertTrue(urls.contains("https://news.com/story"));
    }

    @Test
    public void testExtractUrlsFiltersUnsubscribeLinks() {
        GmailService gmailService = new GmailService();
        
        Message message = new Message();
        MessagePart payload = new MessagePart();
        
        String content = "Check out: https://example.com/article and https://newsletter.com/unsubscribe";
        MessagePartBody body = new MessagePartBody();
        body.setData(java.util.Base64.getEncoder().encodeToString(content.getBytes()));
        
        payload.setBody(body);
        message.setPayload(payload);

        List<String> urls = gmailService.extractUrls(message);
        
        assertEquals(1, urls.size());
        assertTrue(urls.contains("https://example.com/article"));
        assertFalse(urls.contains("https://newsletter.com/unsubscribe"));
    }
}