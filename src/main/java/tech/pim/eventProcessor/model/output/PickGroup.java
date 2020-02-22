package tech.pim.eventProcessor.model.output;

import java.time.Instant;
import java.util.List;

public class PickGroup {
    private String pickerName;
    private Instant activeSince;
    private List<Pick> picks;

    public String getPickerName() {
        return pickerName;
    }

    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }

    public Instant getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(Instant activeSince) {
        this.activeSince = activeSince;
    }

    public List<Pick> getPicks() {
        return picks;
    }

    public void setPicks(List<Pick> picks) {
        this.picks = picks;
    }
}
