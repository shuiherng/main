package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.CalendarChangedEvent;
import seedu.address.model.event.CalendarEvent;

public class CalendarModelManager extends ComponentManager implements CalendarModel {
    private static final Logger logger = LogsCenter.getLogger(CalendarModelManager.class);

    private final Calendar calendar;
    private final FilteredList<CalendarEvent> filteredCalendarEventList;

    /**
     * Initializes a CalendarModelManager with the given calendar and userPrefs.
     */
    public CalendarModelManager(ReadOnlyCalendar calendar, UserPrefs userPrefs) {
        super();
        requireAllNonNull(calendar, userPrefs);

        logger.fine("Initializing with calendar: " + calendar + "and user prefs " + userPrefs);

        this.calendar = new Calendar(calendar);
        this.filteredCalendarEventList = new FilteredList<>(calendar.getAllEventList());
    }

    public CalendarModelManager() { this(new Calendar(), new UserPrefs()); }

    /**
     * Overwrites existing calendar with the new one.
     * @param newData new calendar to replace the existing one.
     */
    @Override
    public void resetData(ReadOnlyCalendar newData) {
        calendar.resetData(newData);
        indicateCalendarChanged();
    }

    @Override
    public ReadOnlyCalendar getCalendar() { return calendar; }

    /** Raises an event to indicate the calendarModel has changed. */
    private void indicateCalendarChanged() {
        raise(new CalendarChangedEvent(calendar));
    }

    @Override
    public boolean hasEvent(CalendarEvent calendarEvent) {
        requireNonNull(calendarEvent);
        return calendar.hasCalendarEvent(calendarEvent);
    }

    @Override
    public void deleteEvent(CalendarEvent target) {
        calendar.removeCalendarEvent(target);
        indicateCalendarChanged();
    }

    @Override
    public void addEvent(CalendarEvent event) {
        calendar.addCalendarEvent(event);
        this.updateFilteredEventList(PREDICATE_SHOW_ALL_CALENDAR_EVENTS);
        indicateCalendarChanged();
    }

    @Override
    public void updateEvent(CalendarEvent target, CalendarEvent editedCalendarEvent) {
        requireAllNonNull(target, editedCalendarEvent);

        calendar.updateCalendarEvent(target, editedCalendarEvent);
        indicateCalendarChanged();
    }


    //=========== Filtered Event List Accessors ==============================

    /**
     * Returns an unmodifiable view of the list of {@code CalendarEvent} backed by the internal list of
     * {@code Calendar}.
     */
    @Override
    public ObservableList<CalendarEvent> getFilteredEventList() {
        return FXCollections.unmodifiableObservableList(filteredCalendarEventList);
    }

    @Override
    public void updateFilteredEventList(Predicate<CalendarEvent> predicate) {
        requireNonNull(predicate);
        filteredCalendarEventList.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CalendarModelManager)) {
            return false;
        }

        CalendarModelManager other = (CalendarModelManager) obj;
        return calendar.equals(other.calendar);
    }

}
