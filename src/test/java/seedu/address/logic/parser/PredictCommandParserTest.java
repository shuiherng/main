package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.PredictCommand;

public class PredictCommandParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    void constructor() throws Exception {
        PredictCommand predictCommand = new PredictCommand("");
        assertEquals(predictCommand, new PredictCommandParser().parse(""));

        PredictCommand predictCommand2 = new PredictCommand("dummy");
        assertNotEquals(predictCommand2, new PredictCommandParser().parse("dum"));
    }
}
