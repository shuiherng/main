package seedu.address.model.event;

import static java.util.Objects.requireNonNull;

/**
 * Represents a EventId for a ScheduleEvent in the calendar for patient book.
 * EventId only increases, and each event is bound to its EventId forever.
 */
public class EventId {

    public static final String MESSAGE_EVENTID_CONSTRAINTS =
            "Event ID should start with e, followed by a sequence of integers.";

    // EventId iterator
    private static int idCounter = 0;

    private static final String ID_VALIDATION_REGEX = "^e[0-9]+$";
    public final String value;

    /**
     * Constructs a {@code EventId}
     */
    public EventId() {
        value = getNewId();
    }

    /**
     * Constructs an {@code EventId} based on an existing ID.
     */
    public EventId(String value, boolean updateMinId) {
        requireNonNull(value);

        this.value = value;
        int idValue = Integer.parseInt(value.substring(1));
        if (idValue >= idCounter && updateMinId) {
            idCounter = idValue + 1;
        }
    }

    private static String getNewId() {
        String id = "e" + Integer.toString(idCounter);
        idCounter += 1;
        return id;
    }

    public static boolean isValidId(String test) {
        return test.matches(ID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventId // instanceof handles nulls
                && value.equals(((EventId) other).value)); // state check
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
