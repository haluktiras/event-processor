package tech.pim.eventProcessor.model;

import java.time.Instant;

public class PickEvent {
    private String id;
    private Instant timestamp;
    private Picker picker;
    private Article article;
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Picker getPicker() {
        return picker;
    }

    public void setPicker(Picker picker) {
        this.picker = picker;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
