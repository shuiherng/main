package seedu.address.model.event.exceptions;

/**
 * Signals that the operation will result in duplicate Schedule Events (Schedule Events are considered
 * duplicates if they have the same identity).
 */
public class DuplicateScheduleEventException extends RuntimeException {
    public DuplicateScheduleEventException() { super ("Operation would result in duplicate calendar event"); }
}


