package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DrugSearchTest {

    private static final int RANDOMIZED_TEST_LENGTH = 8;
    private static final String NOT_IN_DATABASE = "StarbucksCoffee";
    private static final String TOO_GENERIC_1 = "tablet";
    private static final String TOO_GENERIC_2 = "hyd";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void correctResponseTooGeneric() {
        assertEquals(DrugSearch.find(TOO_GENERIC_1), "Too generic.");
        assertEquals(DrugSearch.find(TOO_GENERIC_2), "Too generic.");
    }

    @Test
    public void correctResponseNotInDatabase() {
        assertEquals(DrugSearch.find(NOT_IN_DATABASE),
                "Not found.");
    }

    @Test
    public void isNeverNull() {
        for (int i = 1; i <= 20; i++) {
            assertNotNull(DrugSearch.find(randomStringGenerator(RANDOMIZED_TEST_LENGTH)));
        }
    }

    @Test
    public void moreInfoCacheEmpty() {
        // no drug search has been run yet, therefore results cache is empty
        assertEquals(DrugSearch.moreInfo(1), "Empty.");
    }

    @Test
    public void moreInfoOutOfBounds() {
        DrugSearch.find("glycomet"); // 'Glycomet' is known to be in the database, and to produce
        // fewer than 10 results when searched for
        assertEquals(DrugSearch.moreInfo(11), "Not in cache.");
    }

    /**
     * Generates random, purely alphabetical strings of a given length.
     *
     * @param length Is the length of string to be generated.
     */

    private String randomStringGenerator(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz";

        String str = new Random().ints(length, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());

        return str;
    }
}
