package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
/**
 * Represents a Person's PersonID in the address book.
 * PersonID only increases, and a person is bound to their PersonID forever.
 */
public class PersonID {

    public static final String MESSAGE_PERSONID_CONSTRAINTS =
            "Person ID should start with p, followed by a sequence of integers.";

    // PersonID iterator
    private static int idCounter = 0;

    private static final String ID_VALIDATION_REGEX = "^p[0-9]+$";
    public final String value;

    /**
     * Constructs a {@code PersonID}.
     */
    public PersonID() {
        value = getNewID();
    }

    /**
     * Constructs a {@code PersonID} from an existing ID.
     */
    public PersonID(String value) {
        requireNonNull(value);

        this.value = value;
        int idValue = Integer.parseInt(value.substring(1, value.length()-1));
        if (idValue >= idCounter) {
            idCounter = idValue + 1;
        }
    }

    private static String getNewID(){
        String id = "p"+Integer.toString(idCounter);
        idCounter += 1;
        return id;
    }

    public static boolean isValidID(String test) { return test.matches(ID_VALIDATION_REGEX); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonID // instanceof handles nulls
                && value.equals(((PersonID) other).value)); // state check
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
