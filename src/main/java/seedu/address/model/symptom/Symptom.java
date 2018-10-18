package seedu.address.model.symptom;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Symptom {

    public static final String MESSAGE_SYMPTOM_CONSTRAINTS =
            "symptoms can take any values, and it should not be blank";

    public static final String SYMPTOM_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs an {@code Symptom}.
     *
     * @param symptom A valid Symptom.
     */
    public Symptom(String symptom) {
        requireNonNull(symptom);
        checkArgument(isValidSymptom(symptom), MESSAGE_SYMPTOM_CONSTRAINTS);
        value = symptom;
    }

    /**
     * Returns true if a given string is a valid symptom.
     */
    public static boolean isValidSymptom(String test) {
        return test.matches(SYMPTOM_VALIDATION_REGEX);
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

