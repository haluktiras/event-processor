package tech.pim.eventProcessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Article {
    private String id;
    private String name;
    private TemperatureZone temperatureZone;

    public enum TemperatureZone {
        @JsonProperty("ambient") AMBIENT,
        @JsonProperty("chilled") CHILLED
    }

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

    public TemperatureZone getTemperatureZone() {
        return temperatureZone;
    }

    public void setTemperatureZone(TemperatureZone temperatureZone) {
        this.temperatureZone = temperatureZone;
    }
}
