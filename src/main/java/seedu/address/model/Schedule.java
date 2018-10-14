package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.event.UniqueScheduleEventList;

/**
 * Wraps all data at the calendar level
 * Duplicates are not allowed (by .isSameEvent comparison)
 */

public class Schedule implements ReadOnlySchedule {

    private final UniqueScheduleEventList eventList;

    {
        eventList = new UniqueScheduleEventList();
    }

    public Schedule() {}

    /**
     * Creates a Schedule using the CalendarEvents in the {@code toBeCopied}
     */
    public Schedule(ReadOnlySchedule toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the calendar event list with {@code scheduleEvents}.
     * {@code scheduleEvents} must not contain duplicate events.
     */
    public void setScheduleEvents(List<ScheduleEvent> scheduleEvents) {
        this.eventList.setScheduleEvents(scheduleEvents);
    }

    /**
     * Resets the existing data of this {@code Schedule} with {@code newData}.
     */
    public void resetData(ReadOnlySchedule newData) {
        requireNonNull(newData);

        setScheduleEvents(newData.getAllEventList());
    }

    //// event-level operations

    /**
     * Returns true calendar event already exists in the calendar.
     */
    public boolean hasScheduleEvent(ScheduleEvent event) {
        requireNonNull(event);
        return eventList.contains(event);
    }

    /**
     * Adds a calendar event to the calendar.
     * Event must not already exist in the calendar.
     */
    public void addScheduleEvent(ScheduleEvent e) {
        eventList.add(e);
    }

    /**
     * Replaces the given calendar event {@code target} in the list with {@code editedScheduleEvent}.
     * {@code target} must exist in the calendar.
     */
    public void updateScheduleEvent(ScheduleEvent target, ScheduleEvent editedScheduleEvent) {
        requireNonNull(editedScheduleEvent);

        eventList.setScheduleEvent(target, editedScheduleEvent);
    }

    /**
     * Removes {@code key} from this {@code Schedule}.
     * {@code key} must exist in the calendar.
     */
    public void removeScheduleEvent(ScheduleEvent key) {
        eventList.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return getAllEventList().size() + " events.";
    }

    @Override
    public ObservableList<ScheduleEvent> getAllEventList() {
        return eventList.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Schedule) // instanceof handles nulls
                && eventList.equals(((Schedule) other).eventList);
    }

    @Override
    public int hashCode() {
        return eventList.hashCode();
    }

}
