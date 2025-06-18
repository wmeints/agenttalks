package nl.fizzylogic.newscast.content.service;

import com.google.api.services.gmail.model.Message;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.fizzylogic.newscast.content.config.GmailConfig;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GmailMonitorService {
    private static final Logger logger = Logger.getLogger(GmailMonitorService.class);

    @Inject
    GmailConfig gmailConfig;

    @Inject
    GmailService gmailService;

    @Inject
    ContentSubmissions contentSubmissions;

    @Scheduled(cron = "{gmail.monitoring.schedule}", 
               skipExecutionIf = Scheduled.ApplicationNotRunning.class)
    public void checkForNewMessages() {
        if (!gmailConfig.enabled()) {
            logger.debug("Gmail monitoring is disabled");
            return;
        }

        logger.info("Starting Gmail monitoring check for new messages");

        try {
            List<Message> unreadMessages = gmailService.getUnreadMessages();
            
            if (unreadMessages.isEmpty()) {
                logger.info("No unread messages found");
                return;
            }

            for (Message messageRef : unreadMessages) {
                processMessage(messageRef.getId());
            }

            logger.infof("Completed Gmail monitoring check. Processed %d messages", unreadMessages.size());
        } catch (Exception e) {
            logger.error("Error during Gmail monitoring check", e);
        }
    }

    private void processMessage(String messageId) {
        try {
            Message message = gmailService.getMessage(messageId);
            Optional<String> senderOpt = gmailService.getSender(message);

            if (senderOpt.isEmpty()) {
                logger.warnf("Could not determine sender for message %s, deleting", messageId);
                gmailService.deleteMessage(messageId);
                return;
            }

            String sender = senderOpt.get();
            logger.infof("Processing message %s from %s", messageId, sender);

            if (!gmailService.isFromAllowedSender(sender)) {
                logger.infof("Message from %s is not from an allowed sender, deleting", sender);
                gmailService.deleteMessage(messageId);
                return;
            }

            List<String> urls = gmailService.extractUrls(message);
            
            if (urls.isEmpty()) {
                logger.infof("No URLs found in message %s from %s, marking as read", messageId, sender);
            } else {
                logger.infof("Found %d URLs in message %s from %s", urls.size(), messageId, sender);
                
                for (String url : urls) {
                    try {
                        contentSubmissions.submitContent(url);
                        logger.infof("Submitted content URL: %s", url);
                    } catch (Exception e) {
                        logger.errorf(e, "Failed to submit content URL: %s", url);
                    }
                }
            }

            // Mark the message as read after processing
            gmailService.markAsRead(messageId);

        } catch (Exception e) {
            logger.errorf(e, "Failed to process message %s", messageId);
        }
    }
}