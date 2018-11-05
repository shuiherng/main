package seedu.address.model.symptom;

import org.junit.Test;
import seedu.address.testutil.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SymptomTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Symptom(null));
    }

    @Test
    public void check_equal() {

        Symptom fever = new Symptom("fever");
        Symptom FEVER = new Symptom("FEVER");
        Symptom fever2 = new Symptom("fever");
        Symptom empty = new Symptom("");
        assertEquals(fever, fever2);
        assertNotEquals(fever, FEVER);
        assertNotEquals(FEVER, empty);

    }

}