package nl.infosupport.agenttalks.shared;

import java.util.List;

import org.eclipse.microprofile.graphql.NonNull;

public class PagedResultSet<T> {
    @NonNull
    public List<T> items;

    @NonNull
    public int totalItems;

    @NonNull
    public int totalPages;

    @NonNull
    public int pageIndex;

    @NonNull
    public int pageSize;

    public PagedResultSet(List<T> items, int totalItems, int pageIndex, int pageSize) {
        this.items = items;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
