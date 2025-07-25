package nl.infosupport.agenttalks.reader;

public class ContentDownloadFailedException extends RuntimeException {
    public ContentDownloadFailedException(String message) {
        super(message);
    }

    public ContentDownloadFailedException(String message, Throwable inner) {
        super(message, inner);
    }
}
