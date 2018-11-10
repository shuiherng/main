package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
/**
 * Represents a Person's PersonId in the address book.
 * PersonId only increases, and a person is bound to their PersonId forever.
 */
public class PersonId {

    public static final String MESSAGE_PERSONID_CONSTRAINTS =
            "Person ID should start with p, followed by a sequence of integers.";

    // PersonId iterator
    private static int idCounter = 0;

    private static final String ID_VALIDATION_REGEX = "^p[0-9]+$";
    public final String value;

    /**
     * Constructs a {@code PersonId}.
     */
    public PersonId() {
        value = getNewId();
    }

    /**
     * Constructs a {@code PersonId} from an existing ID.
     */
    public PersonId(String value, boolean changeMinId) {
        requireNonNull(value);

        this.value = value;
        int idValue = Integer.parseInt(value.substring(1));
        if (idValue >= idCounter && changeMinId) {
            idCounter = idValue + 1;
        }
    }

    private static String getNewId() {
        String id = "p" + Integer.toString(idCounter);
        idCounter += 1;
        return id;
    }

    public static boolean isValidId(String test) { return test.matches(ID_VALIDATION_REGEX); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonId // instanceof handles nulls
                && value.equals(((PersonId) other).value)); // state check
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
