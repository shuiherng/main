package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.event.UniqueScheduleEventList;

import java.util.List;
import static java.util.Objects.requireNonNull;

public class ModelSchedule implements ReadOnlySchedule {
    
    private final UniqueScheduleEventList events;

    {
        events = new UniqueScheduleEventList();
    }

    public ModelSchedule() {}

    /**
     * Creates a Calender using the CalendarEvents in the {@code toBeCopied}
     */
    public ModelSchedule(ReadOnlySchedule toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setEvents(List<ScheduleEvent> events) {
        this.events.setScheduleEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlySchedule newData) {
        requireNonNull(newData);

        setEvents(newData.getAllEventList());
    }

    /**
     * Returns true if an event with the same identity as {@code event} exists in the calendar.
     */
    public boolean hasEvent(ScheduleEvent event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the Calendar.
     * The event must not already exist in the calendar.
     */
    public void addEvent(ScheduleEvent e) {
        events.add(e);
    }

    /**
     * Replaces the given event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in the Calendar.
     * The event identity of {@code editedEvent} must not be the same as another existing event in the calendar.
     */
    public void updateEvent(ScheduleEvent target, ScheduleEvent editedEvent) {
        requireNonNull(editedEvent);
        events.setScheduleEvent(target, editedEvent);
    }

    /**
     * Removes {@code key} from this {@code Calendar}.
     * {@code key} must exist in the Calendar.
     *
     * Note: Calendar removal is done via soft-delete
     */
    public void removeEvent(ScheduleEvent key) {
        events.remove(key);
    }

    @Override
    public String toString() {
        return getAllEventList().size() + " events\n";
    }

    @Override
    public ObservableList<ScheduleEvent> getAllEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public int hashCode() {
        return events.hashCode();
    }
}
