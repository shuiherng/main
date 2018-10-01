package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.CalendarEvent;

/**
 * Unmodifiable view of the calendar.
 */
public interface ReadOnlyCalendar {

    /**
     * Returns an unmodifiable view of the calendar.
     * This list will not contain any duplicate events.
     */
    ObservableList<CalendarEvent> getAllEventList();

}
