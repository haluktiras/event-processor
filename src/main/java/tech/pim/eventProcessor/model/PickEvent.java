package tech.pim.eventProcessor.model;

import lombok.Data;
import java.time.Instant;

@Data
public class PickEvent {

    private String id;
    private Instant timestamp;
    private Picker picker;
    private Article article;
    private int quantity;
}
