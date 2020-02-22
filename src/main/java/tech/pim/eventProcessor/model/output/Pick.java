package tech.pim.eventProcessor.model.output;

import lombok.Data;
import java.time.Instant;

@Data
public class Pick {

    private String articleName;
    private Instant timestamp;
}
