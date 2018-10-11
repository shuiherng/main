package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.CalendarEvent;
import seedu.address.model.event.UniqueCalendarEventList;
import java.util.Calendar;
import java.util.List;
import static java.util.Objects.requireNonNull;

public class Calender implements ReadOnlyCalendar {
    
    private final UniqueCalendarEventList events;

    {
        events = new UniqueCalendarEventList();
    }

    public Calender() {}

    /**
     * Creates a Calender using the CalendarEvents in the {@code toBeCopied}
     */
    public Calender(ReadOnlyCalendar toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setEvents(List<CalendarEvent> events) {
        this.events.setCalendarEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyCalendar newData) {
        requireNonNull(newData);

        setEvents(newData.getAllEventList());
    }
    @Override
    public ObservableList<CalendarEvent> getAllEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public int hashCode() {
        return events.hashCode();
    }
}
