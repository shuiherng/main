package seedu.address.model.symptom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class SymptomTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Symptom(null));
    }

    @Test
    public void check_equal() {

        Symptom fever = new Symptom("fever");
        Symptom feverCap = new Symptom("FEVER");
        Symptom fever2 = new Symptom("fever");
        Symptom empty = new Symptom("");
        assertEquals(fever, fever2);
        assertNotEquals(fever, feverCap);
        assertNotEquals(feverCap, empty);

    }

}
