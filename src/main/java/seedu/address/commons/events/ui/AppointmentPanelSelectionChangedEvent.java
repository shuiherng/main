package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.event.ScheduleEvent;
// import seedu.address.model.person.Person;

/**
 * Represents a selection change in the Appointment Panel
 */
public class AppointmentPanelSelectionChangedEvent extends BaseEvent {


    private final ScheduleEvent newSelection;

    public AppointmentPanelSelectionChangedEvent(ScheduleEvent newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public ScheduleEvent getNewSelection() {
        return newSelection;
    }
}
