package seedu.address.model;

import java.util.Calendar;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.event.EventId;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.event.exceptions.ScheduleEventNotFoundException;

/**
 * The API of the ScheduleModel component.
 */
public interface ScheduleModel {
    /** {@code Predicate} that shows all future events */
    Predicate<ScheduleEvent> PREDICATE_SHOW_SCHEDULE_EVENTS = e -> {
        Calendar currentDay = Calendar.getInstance();
        return e.getDate().getKey().compareTo(currentDay) > 0;
    };

    /** {@code Predicate} that shows all events, including those in the past */
    Predicate<ScheduleEvent> PREDICATE_SHOW_ALL_SCHEDULE_EVENTS = unused -> true;

    /** Clears existing backing ScheduleModel and replaces with the provided new data. */
    void resetData(ReadOnlySchedule newData);

    /** Returns the Schedule. */
    ReadOnlySchedule getSchedule();

    /**
     * Returns true if a calenadr event with the same identify as {@code scheduleEvent} exists in the calendar.
     */
    boolean hasEvent(ScheduleEvent scheduleEvent);

    /**
     * Deletes a given event.
     * The event must exist in the calendar.
     */
    void deleteEvent(ScheduleEvent target);

    /**
     * Adds the given event.
     * {@code scheduleEvent} must not already exist in the calendar.
     */
    void addEvent(ScheduleEvent scheduleEvent);

    /**
     * Replaces the given event {@code target} with {@code updatedScheduleEvent}.
     * {@code target} must exist in the calendar.
     */
    void updateEvent(ScheduleEvent target, ScheduleEvent updatedScheduleEvent);

    /**
     * Returns an unmodifiable view of the filtered event list.
     */
    ObservableList<ScheduleEvent> getFilteredEventList();

    /**
     * Updates the filter of the filtered event list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<ScheduleEvent> predicate);

    /**
     * Internally used function to get a list of persons without updating the UI.
     * @param predicate predicate to match.
     * @return list of filtered schedule events, sorted by start time.
     */
    ObservableList<ScheduleEvent> internalGetFromEventList(Predicate<ScheduleEvent> predicate);

    /**
     * Finds a person by their Id. O
     * @param eventId Lookup id.
     * @return ScheduleEvent object.
     * @throws ScheduleEventNotFoundException
     */
    ScheduleEvent getEventById(EventId eventId) throws ScheduleEventNotFoundException;
}
