package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.model.symptom.Symptom;
import seedu.address.testutil.Assert;

public class CommandResultTest {

    private final CommandResult emptyFeedback = new CommandResult("");

    private final CommandResult normalFeedback = new CommandResult("This is a test string.");

    @Test
    public void constructor() {
        Assert.assertThrows(NullPointerException.class, () -> new CommandResult(null));
    }

    @Test
    public void convertListToString() {
        List<?> emptyList = new ArrayList<>();

        List<Symptom> symptomList = new ArrayList<>();
        symptomList.add(new Symptom("lonely"));
        symptomList.add(new Symptom("sad"));

        Assert.assertThrows(StringIndexOutOfBoundsException.class, () -> CommandResult.convertListToString(emptyList));
        assertNotNull(CommandResult.convertListToString(symptomList));
    }

    @Test
    public void equals() {
        assertNotEquals(emptyFeedback, normalFeedback);
    }

    @Test
    public void toStringTest() {
        assertNotNull(emptyFeedback.toString());
        assertTrue(emptyFeedback.toString().contains("CommandResult: "));

        assertNotNull(normalFeedback.toString());
        assertFalse(normalFeedback.toString().contains("Random String"));
    }
}
