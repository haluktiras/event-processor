package tech.pim.eventProcessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Article {

    private String id;
    private String name;
    private TemperatureZone temperatureZone;

    public enum TemperatureZone {
        @JsonProperty("ambient") AMBIENT,
        @JsonProperty("chilled") CHILLED
    }
}
