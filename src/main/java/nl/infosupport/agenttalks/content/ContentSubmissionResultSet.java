package nl.infosupport.agenttalks.content;

import java.util.List;

import nl.infosupport.agenttalks.shared.PagedResultSet;

public class ContentSubmissionResultSet extends PagedResultSet<ContentSubmission> {
    public ContentSubmissionResultSet(List<ContentSubmission> items, int totalItems, int pageIndex, int pageSize) {
        super(items, totalItems, pageIndex, pageSize);
    }
}
