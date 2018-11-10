package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DrugSearchTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String NOT_IN_DATABASE = "StarbucksCoffee";
    private static final String TOO_GENERIC_1 = "tablet";
    private static final String TOO_GENERIC_2 = "hyd";
    private static final int RANDOMIZED_TEST_LENGTH = 8;

    @Test
    public void correctResponseTooGeneric() {
        assertEquals(DrugSearch.find(TOO_GENERIC_1), "Too generic.");
        assertEquals(DrugSearch.find(TOO_GENERIC_2), "Too generic.");
    }

    @Test
    public void correctResponseNotInDatabase() {
        assertEquals(DrugSearch.find(NOT_IN_DATABASE), "Not found.");
    }

    @Test
    public void isNeverNull() {
        for (int i = 1; i <= 20; i++) {
            assertNotNull(DrugSearch.find(randomStringGenerator(RANDOMIZED_TEST_LENGTH)));
        }
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
