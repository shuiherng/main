package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.*;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyAddressBook_success() {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        AddressBookModel expectedAddressBookModel = new AddressBookModelManager();
        expectedAddressBookModel.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), addressBookModel, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedAddressBookModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        AddressBookModel addressBookModel = new AddressBookModelManager(getTypicalAddressBook(), new UserPrefs());
        AddressBookModel expectedAddressBookModel = new AddressBookModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedAddressBookModel.resetData(new AddressBook());
        expectedAddressBookModel.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), addressBookModel, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedAddressBookModel);
    }

}
