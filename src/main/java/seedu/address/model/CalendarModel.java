package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.event.CalendarEvent;

/**
 * The API of the CalendarModel component.
 */
public interface CalendarModel {
    /** {@code Predicate} that always evaluates to true */
    Predicate<CalendarEvent> PREDICATE_SHOW_ALL_CALENDAR_EVENTS = unused -> true;

    /** Clears existing backing CalendarModel and replaces with the provided new data. */
    void resetData(ReadOnlyCalendar newData);

    /** Returns the Calendar. */
    ReadOnlyCalendar getCalendar();

    /**
     * Returns true if a calenadr event with the same identify as {@code calendarEvent} exists in the calendar.
     */
    boolean hasEvent(CalendarEvent calendarEvent);

    /**
     * Deletes a given event.
     * The event must exist in the calendar.
     */
    void deleteEvent(CalendarEvent target);

    /**
     * Adds the given event.
     * {@code calendarEvent} must not already exist in the calendar.
     */
    void addEvent(CalendarEvent calendarEvent);

    /**
     * Replaces the given event {@code target} with {@code updatedCalendarEvent}.
     * {@code target} must exist in the calendar.
     */
    void updateEvent(CalendarEvent target, CalendarEvent updatedCalendarEvent);

    /**
     * Returns an unmodifiable view of the filtered event list.
     */
    ObservableList<CalendarEvent> getFilteredEventList();

    /**
     * Updates the filter of the filtered event list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<CalendarEvent> predicate);

}
