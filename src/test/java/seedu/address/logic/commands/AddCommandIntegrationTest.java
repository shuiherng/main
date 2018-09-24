package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the AddressBookModel) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private AddressBookModel addressBookModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        addressBookModel = new AddressBookModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        AddressBookModel expectedAddressBookModel = new AddressBookModelManager(addressBookModel.getAddressBook(), new UserPrefs());
        expectedAddressBookModel.addPerson(validPerson);
        expectedAddressBookModel.commitAddressBook();

        assertCommandSuccess(new AddCommand(validPerson), addressBookModel, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validPerson), expectedAddressBookModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = addressBookModel.getAddressBook().getAllPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), addressBookModel, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
