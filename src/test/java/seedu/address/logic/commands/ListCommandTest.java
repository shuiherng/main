package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the AddressBookModel) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private AddressBookModel addressBookModel;
    private AddressBookModel expectedAddressBookModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        addressBookModel = new AddressBookModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedAddressBookModel = new AddressBookModelManager(addressBookModel.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), addressBookModel, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedAddressBookModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(addressBookModel, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), addressBookModel, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedAddressBookModel);
    }
}
