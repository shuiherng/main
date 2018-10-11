package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.ScheduleEvent;

/**
 * Unmodifiable view of the calendar.
 */
public interface ReadOnlySchedule {

    /**
     * Returns an unmodifiable view of the calendar.
     * This list will not contain any duplicate events.
     */
    ObservableList<ScheduleEvent> getAllEventList();

}
