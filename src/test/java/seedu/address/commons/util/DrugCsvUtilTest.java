package seedu.address.commons.util;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DrugCsvUtilTest {

    private static final String TEST_KEYWORD = "Lyrica";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void returnsNullEventually() throws Exception {
        DrugCsvUtil testutil = new DrugCsvUtil(TEST_KEYWORD);
        String[] nextRecord;
        while ((nextRecord = testutil.nextMatchingEntry()) != null) {
            // do nothing
        }
        for (int i = 1; i <= 5; i++) {
            assertTrue((nextRecord = testutil.nextMatchingEntry()) == null);
        }
    }
}
