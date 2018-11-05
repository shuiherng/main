package seedu.address.model.symptom;

import org.junit.Test;

import seedu.address.testutil.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DiseaseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Disease(null));
    }

    @Test
    public void check_equal() {

        Disease influenza = new Disease("influenza");
        Disease INFLUENZA = new Disease("INFLUENZA");
        Disease influenza2 = new Disease("influenza");
        Disease empty_string = new Disease("");
        assertEquals(influenza, influenza2);
        assertNotEquals(influenza, INFLUENZA);
        assertNotEquals(INFLUENZA, empty_string);

    }

}