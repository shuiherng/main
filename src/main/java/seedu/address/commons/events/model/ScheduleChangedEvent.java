package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlySchedule;

/** Indicates the Schedule in the ScheduleModel has changed. */
public class ScheduleChangedEvent extends BaseEvent {

    public final ReadOnlySchedule data;

    public ScheduleChangedEvent(ReadOnlySchedule data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of events " + data.getAllEventList().size();
    }
}
