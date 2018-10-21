package seedu.address.model.symptom;

import static java.util.Objects.requireNonNull;

/**
 * Represents disease.
 */
public class Disease {

    private final String value;

    /**
     * Constructs an {@code Disease}.
     *
     * @param disease A valid disease.
     */
    public Disease(String disease) {
        requireNonNull(disease);
        value = disease;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Disease // instanceof handles nulls
                && value.equals(((Disease) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

