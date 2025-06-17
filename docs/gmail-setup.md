# Gmail Monitoring Setup

This guide explains how to configure the Gmail monitoring feature to automatically extract content URLs from incoming emails.

## Prerequisites

1. A Gmail account for monitoring
2. Google Cloud Project with Gmail API enabled
3. Service account credentials JSON file

## Google Cloud Setup

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the Gmail API:
   - Go to "APIs & Services" > "Library"
   - Search for "Gmail API" and enable it
4. Create service account credentials:
   - Go to "APIs & Services" > "Credentials"
   - Click "Create Credentials" > "Service Account"
   - Download the JSON credentials file
5. Enable domain-wide delegation for the service account (if using G Suite)

## Application Configuration

Add the following properties to your `application.properties`:

```properties
# Enable Gmail monitoring
gmail.monitoring.enabled=true

# Check for new emails every 5 minutes (default)
gmail.monitoring.schedule=0 */5 * * * ?

# Path to your Google API credentials JSON file
gmail.monitoring.credentials-path=/path/to/your/credentials.json

# List of allowed sender email addresses
gmail.monitoring.allowed-senders=joop@example.com,owner@example.com

# Application name for Gmail API
gmail.monitoring.application-name=Quarkus Newscast
```

## How It Works

1. **Scheduled Monitoring**: The system checks for unread emails based on the configured schedule
2. **Sender Validation**: Only emails from allowed senders are processed
3. **URL Extraction**: The system automatically extracts URLs from email content
4. **Content Submission**: Valid URLs are submitted to the content management system
5. **Email Cleanup**: 
   - Processed emails are marked as read
   - Emails from unauthorized senders are deleted

## Configuration Options

| Property | Default | Description |
|----------|---------|-------------|
| `gmail.monitoring.enabled` | `false` | Enable/disable Gmail monitoring |
| `gmail.monitoring.schedule` | `0 */5 * * * ?` | Cron expression for check frequency |
| `gmail.monitoring.credentials-path` | - | Path to Google API credentials file |
| `gmail.monitoring.allowed-senders` | - | Comma-separated list of allowed email addresses |
| `gmail.monitoring.application-name` | `Quarkus Newscast` | Application name for Gmail API |

## Testing

With Gmail monitoring disabled by default in test mode, you can run tests without credentials:

```bash
./mvnw test
```

## Security Notes

- Store credentials securely and never commit them to source control
- Consider using environment variables for sensitive configuration
- Regularly rotate service account keys
- Monitor Gmail API usage quotas