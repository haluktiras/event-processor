package tech.pim.eventProcessor.model.output;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class PickGroup {
    private String pickerName;
    private Instant activeSince;
    private List<Pick> picks;
}
