package seedu.address.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.PersonUtil.matchProperties;

public class EditCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parse_patient_success() throws Exception {
        Person person = new PersonBuilder().build();
        Person editedPerson = new PersonBuilder().withPhone(VALID_PHONE_AMY).build();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(PREFIX_PHONE
                + VALID_PHONE_AMY, PREFIX_NAME);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        person = new PersonBuilder().build();

    }

    private Person testAddressBookModel(Person original, ArgumentMultimap argMultimap)
            throws Exception {

        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory =  new CommandHistory();

        addressBookModel.addPerson(original);

        EditCommand cmd = new EditCommand(CMDTYPE_PATIENT, original.getId().toString(), argMultimap);

        cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);

        // added person should be in index 0
        return addressBookModel.internalGetFromPersonList(unused -> true).get(0);
    }
}
