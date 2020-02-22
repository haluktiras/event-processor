package tech.pim.eventProcessor.model;

import java.time.Instant;
public class Picker {
    private String id;
    private String name;
    private Instant activeSince;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(Instant activeSince) {
        this.activeSince = activeSince;
    }
}
