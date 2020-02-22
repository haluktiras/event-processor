package tech.pim.eventProcessor.model;

import lombok.Data;
import java.time.Instant;

@Data
public class Picker {

    private String id;
    private String name;
    private Instant activeSince;
}
