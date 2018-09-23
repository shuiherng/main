package seedu.address.model.person;

/**
 * Represents a Person's ID in the address book.
 * ID only increases, and a person is bound to their ID forever.
 */
public class ID {

    // ID iterator
    public static int idCounter = 0;

    public static final String ID_VALIDATION_REGEX = "^p[0-9]+$";
    public final String value;

    /**
     * Constructs a {@code ID}.
     */
    public ID() {
        value = getNewID();
    }
    private static String getNewID(){
        String id = new String("p"+Integer.toString(idCounter));
        idCounter += 1;
        return id;
    }

    public static boolean isValidID(String test) { return test.matches(ID_VALIDATION_REGEX); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ID // instnaceof handles nulls
                && value.equals(((ID) other).value)); // state check
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
