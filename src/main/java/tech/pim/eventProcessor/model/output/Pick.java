package tech.pim.eventProcessor.model.output;

import java.time.Instant;

public class Pick {
    private String articleName;
    private Instant timestamp;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
