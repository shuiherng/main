package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListPersonCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.*;


public class LogicManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AddressBookModel addressBookModel = new AddressBookModelManager();
    private ScheduleModel scheduleModel = new ScheduleModelManager();
    private Logic logic = new LogicManager(addressBookModel, scheduleModel);

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
        assertHistoryCorrect(invalidCommand);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertHistoryCorrect(deleteCommand);
    }

    @Test
    public void execute_validCommand_success() {
        String listCommand = ListPersonCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListPersonCommand.MESSAGE_SUCCESS, addressBookModel);
        assertHistoryCorrect(listCommand);
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        logic.getFilteredPersonList().remove(0);
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedAddressBookModel} is as specified.
     * @see #assertCommandBehavior(Class, String, String, AddressBookModel)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, AddressBookModel expectedAddressBookModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedAddressBookModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, AddressBookModel)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, AddressBookModel)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, AddressBookModel)
     */
    private void assertCommandFailure(String inputCommand, Class<?> expectedException, String expectedMessage) {
        AddressBookModel expectedAddressBookModel = new AddressBookModelManager(addressBookModel.getAddressBook(), new UserPrefs());
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedAddressBookModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     *      - the internal addressBookModel manager data are same as those in the {@code expectedAddressBookModel} <br>
     *      - {@code expectedAddressBookModel}'s address book was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                           String expectedMessage, AddressBookModel expectedAddressBookModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedException, null);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedAddressBookModel, addressBookModel);
    }

    /**
     * Asserts that the result display shows all the {@code expectedCommands} upon the execution of
     * {@code HistoryCommand}.
     */
    private void assertHistoryCorrect(String... expectedCommands) {
        try {
            CommandResult result = logic.execute(HistoryCommand.COMMAND_WORD);
            String expectedMessage = String.format(
                    HistoryCommand.MESSAGE_SUCCESS, String.join("\n", expectedCommands));
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (ParseException | CommandException e) {
            throw new AssertionError("Parsing and execution of HistoryCommand.COMMAND_WORD should succeed.", e);
        }
    }
}
