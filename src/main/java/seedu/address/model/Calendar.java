package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.event.CalendarEvent;
import seedu.address.model.event.UniqueCalendarEventList;

/**
 * Wraps all data at the calendar level
 * Duplicates are not allowed (by .isSameEvent comparison)
 */

public class Calendar implements ReadOnlyCalendar {

    private final UniqueCalendarEventList eventList;

    {
        eventList = new UniqueCalendarEventList();
    }

    public Calendar() {}

    /**
     * Creates a Calendar using the CalendarEvents in the {@code toBeCopied}
     */
    public Calendar(ReadOnlyCalendar toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the calendar event list with {@code calendarEvents}.
     * {@code calendarEvents} must not contain duplicate events.
     */
    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        this.eventList.setCalendarEvents(calendarEvents);
    }

    /**
     * Resets the existing data of this {@code Calendar} with {@code newData}.
     */
    public void resetData(ReadOnlyCalendar newData) {
        requireNonNull(newData);

        setCalendarEvents(newData.getAllEventList());
    }

    //// event-level operations

    /**
     * Returns true calendar event already exists in the calendar.
     */
    public boolean hasCalendarEvent(CalendarEvent event) {
        requireNonNull(event);
        return eventList.contains(event);
    }

    /**
     * Adds a calendar event to the calendar.
     * Event must not already exist in the calendar.
     */
    public void addCalendarEvent(CalendarEvent e) {
        eventList.add(e);
    }

    /**
     * Replaces the given calendar event {@code target} in the list with {@code editedCalendarEvent}.
     * {@code target} must exist in the calendar.
     */
    public void updateCalendarEvent(CalendarEvent target, CalendarEvent editedCalendarEvent) {
        requireNonNull(editedCalendarEvent);

        eventList.setCalendarEvent(target, editedCalendarEvent);
    }

    /**
     * Removes {@code key} from this {@code Calendar}.
     * {@code key} must exist in the calendar.
     */
    public void removeCalendarEvent(CalendarEvent key) {
        eventList.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return getAllEventList().size() + " events.";
    }

    @Override
    public ObservableList<CalendarEvent> getAllEventList() {
        return eventList.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Calendar) // instanceof handles nulls
                && eventList.equals(((Calendar) other).eventList);
    }

    @Override
    public int hashCode() {
        return eventList.hashCode();
    }

}
