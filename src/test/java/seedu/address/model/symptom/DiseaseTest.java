package seedu.address.model.symptom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DiseaseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Disease(null));
    }

    @Test
    public void check_equal() {

        Disease influenza = new Disease("influenza");
        Disease influenzaCap = new Disease("INFLUENZA");
        Disease influenza2 = new Disease("influenza");
        Disease emptyString = new Disease("");
        assertEquals(influenza, influenza2);
        assertNotEquals(influenza, influenzaCap);
        assertNotEquals(influenza, emptyString);

    }

}
