package nl.fizzylogic.newscast.content.graph.input;

import org.eclipse.microprofile.graphql.Input;

@Input
public class SummarizeContent {
    public long id;
    public String title;
    public String summary;

    public SummarizeContent() {
        // Default constructor for deserialization
    }

    public SummarizeContent(long id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }
}
