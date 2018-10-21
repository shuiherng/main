package seedu.address.model.symptom;

import static java.util.Objects.requireNonNull;

/**
 * Represents symptom.
 */
public class Symptom {

    private final String value;

    /**
     * Constructs an {@code Symptom}.
     *
     * @param symptom A valid Symptom.
     */
    public Symptom(String symptom) {
        requireNonNull(symptom);
        value = symptom;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Symptom // instanceof handles nulls
                && value.equals(((Symptom) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

