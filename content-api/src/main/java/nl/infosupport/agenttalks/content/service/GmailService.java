package nl.infosupport.agenttalks.content.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.infosupport.agenttalks.content.config.GmailConfig;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class GmailService {
    private static final Logger logger = Logger.getLogger(GmailService.class);
    private static final String SCOPES = "https://www.googleapis.com/auth/gmail.modify";
    private static final Pattern URL_PATTERN = Pattern.compile("https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]+");

    @Inject
    GmailConfig gmailConfig;

    public List<Message> getUnreadMessages() throws IOException, GeneralSecurityException {
        if (!gmailConfig.enabled() || gmailConfig.credentialsPath().isEmpty()) {
            logger.info("Gmail monitoring is disabled or credentials path not configured");
            return Collections.emptyList();
        }

        Gmail service = buildGmailService();
        String userId = "me";
        String query = "is:unread";

        try {
            ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
            List<Message> messages = response.getMessages();

            if (messages == null) {
                return Collections.emptyList();
            }

            logger.infof("Found %d unread messages", messages.size());
            return messages;
        } catch (IOException e) {
            logger.error("Failed to retrieve unread messages", e);
            throw e;
        }
    }

    public Message getMessage(String messageId) throws IOException, GeneralSecurityException {
        Gmail service = buildGmailService();
        return service.users().messages().get("me", messageId).execute();
    }

    public void markAsRead(String messageId) throws IOException, GeneralSecurityException {
        Gmail service = buildGmailService();
        service.users().messages().modify("me", messageId,
                new com.google.api.services.gmail.model.ModifyMessageRequest()
                        .setRemoveLabelIds(Arrays.asList("UNREAD")))
                .execute();
        logger.infof("Marked message %s as read", messageId);
    }

    public void deleteMessage(String messageId) throws IOException, GeneralSecurityException {
        Gmail service = buildGmailService();
        service.users().messages().delete("me", messageId).execute();
        logger.infof("Deleted message %s", messageId);
    }

    public Optional<String> getSender(Message message) {
        if (message.getPayload() == null || message.getPayload().getHeaders() == null) {
            return Optional.empty();
        }

        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            if ("From".equals(header.getName())) {
                String from = header.getValue();
                // Extract email address from "Name <email@domain.com>" format
                if (from.contains("<") && from.contains(">")) {
                    int start = from.indexOf('<') + 1;
                    int end = from.indexOf('>');
                    return Optional.of(from.substring(start, end));
                }
                return Optional.of(from);
            }
        }
        return Optional.empty();
    }

    public List<String> extractUrls(Message message) {
        String content = getMessageContent(message);
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }

        Matcher matcher = URL_PATTERN.matcher(content);
        return matcher.results()
                .map(result -> result.group())
                .filter(url -> !url.toLowerCase().contains("unsubscribe")) // Filter out unsubscribe links
                .distinct()
                .toList();
    }

    public boolean isFromAllowedSender(String senderEmail) {
        if (gmailConfig.allowedSenders().isEmpty()) {
            logger.warn("No allowed senders configured - no emails will be processed");
            return false;
        }

        List<String> allowedSenders = gmailConfig.allowedSenders().get();
        boolean allowed = allowedSenders.contains(senderEmail.toLowerCase());

        if (!allowed) {
            logger.infof("Email from %s is not from an allowed sender. Allowed senders: %s",
                    senderEmail, allowedSenders);
        }

        return allowed;
    }

    private Gmail buildGmailService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(gmailConfig.credentialsPath().get()))
                .createScoped(SCOPES);

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(gmailConfig.applicationName())
                .build();
    }

    private String getMessageContent(Message message) {
        if (message.getPayload() == null) {
            return null;
        }

        MessagePart payload = message.getPayload();
        String body = extractTextFromPart(payload);

        return body != null ? body : "";
    }

    private String extractTextFromPart(MessagePart part) {
        if (part.getBody() != null && part.getBody().getData() != null) {
            byte[] data = part.getBody().decodeData();
            return new String(data);
        }

        if (part.getParts() != null) {
            for (MessagePart subPart : part.getParts()) {
                if ("text/plain".equals(subPart.getMimeType()) || "text/html".equals(subPart.getMimeType())) {
                    String text = extractTextFromPart(subPart);
                    if (text != null && !text.isEmpty()) {
                        return text;
                    }
                }
            }
        }

        return null;
    }
}