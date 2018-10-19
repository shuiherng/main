package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateScheduleEventException;
import seedu.address.model.event.exceptions.ScheduleEventNotFoundException;

/**
 * A list of calendar events that enforces uniqueness between its elements and does not allow nulls.
 * A calendar is considered unique by comparing using {@code ScheduleEvent#isSameEvent(ScheduleEvent)}.
 * As such, adding and updating of calendar events uses ScheduleEvent#isSameEvent(ScheduleEvent)
 * for equality so as to ensure that the calendar event being added or updated is unique in
 * terms of identity in the UniqueScheduleEventList. Removal of a calendar event uses
 * ScheduleEvent#equals(Object) to ensure that the correct event is removed.
 *
 * Supports a minimal set of list operations.
 *
 * Developer note: Since we're using an ID-based system to distinguish between calendar events, both
 * ScheduleEvent#isSameEvent(ScheduleEvent) and ScheduleEvent#equals(Object) match the events by ID.
 * However, the methods are left separate so future developers can utilize them.
 *
 * @see ScheduleEvent#isSameEvent(ScheduleEvent)
 */
public class UniqueScheduleEventList implements Iterable<ScheduleEvent> {

    private final ObservableList<ScheduleEvent> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent calendar event as the given argument
     */
    public boolean contains(ScheduleEvent toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEvent);
    }

    /**
     * adds a calendar event to the list.
     * The calendar event must not already exist in the list.
     */
    public void add(ScheduleEvent toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateScheduleEventException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the calendar event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in the list.
     * The event ID of {@code editedEvent} must not be the same as another existing event in the list.
     * Event ID is matched with {@code ScheduleEvent#isSameEvent(ScheduleEvent)}.
     */
    public void setScheduleEvent(ScheduleEvent target, ScheduleEvent editedEvent) {
        requireAllNonNull(target, editedEvent);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ScheduleEventNotFoundException();
        }

        if (!target.isSameEvent(editedEvent) && contains(editedEvent)) {
            throw new DuplicateScheduleEventException();
        }

        internalList.set(index, editedEvent);
    }

    /**
     * Removes the equivalent calendar event from the list.
     * The calendar event must exist in the list.
     */
    public void remove(ScheduleEvent toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ScheduleEventNotFoundException();
        }
    }

    public void setScheduleEvents(UniqueScheduleEventList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setScheduleEvents(List<ScheduleEvent> events) {
        requireAllNonNull(events);
        if (!eventsAreUnique(events)) {
            throw new DuplicateScheduleEventException();
        }

        internalList.setAll(events);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ScheduleEvent> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<ScheduleEvent> iterator() { return internalList.iterator(); }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueScheduleEventList // instanceof handles nulls
                && internalList.equals(((UniqueScheduleEventList) other).internalList));
    }

    @Override
    public int hashCode() { return internalList.hashCode(); }

    /**
     * Returns true if {@code events} contain only unique events.
     */
    private boolean eventsAreUnique(List<ScheduleEvent> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = i + 1; j < events.size(); j++) {
                if (events.get(i).isSameEvent(events.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
