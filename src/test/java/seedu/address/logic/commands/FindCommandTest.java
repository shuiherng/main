package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DISEASE;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.symptom.Disease;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalEvents;

/**
 * Tests the find command.
 */
public class FindCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void find_patient_success() throws Exception {
        AddressBookModel model = new AddressBookModelManager();
        Person p1 = new PersonBuilder().withName(VALID_NAME_AMY).build();
        Person p2 = new PersonBuilder().withName(VALID_NAME_BOB).build();
        model.addPerson(p1);
        model.addPerson(p2);

        // ensure that only p1 is found in the filtered list
        assertFoundInAddressBook(model, VALID_NAME_AMY);
        assertTrue(model.getFilteredPersonList().size() == 1);
        assertEquals(model.getFilteredPersonList().get(0), p1);


        // reset filtered list for next test
        model.updateFilteredPersonList(AddressBookModel.PREDICATE_SHOW_ALL_EXISTING_PERSONS);

        // ensure that only p2 is found in the filtered list
        assertFoundInAddressBook(model, p2.getId().toString());
        assertTrue(model.getFilteredPersonList().size() == 1);
        assertEquals(model.getFilteredPersonList().get(0), p2);


        // reset filtered list for next test
        model.updateFilteredPersonList(AddressBookModel.PREDICATE_SHOW_ALL_EXISTING_PERSONS);

        // ensure that both amy and bob are both found in filtered list
        assertFoundInAddressBook(model, VALID_NAME_AMY + " " + VALID_NAME_BOB);
        assertTrue(model.getFilteredPersonList().size() == 2);


        // reset filtered list for next test
        model.updateFilteredPersonList(AddressBookModel.PREDICATE_SHOW_ALL_EXISTING_PERSONS);

        // ensure that there are no entries in filtered list
        assertFoundInAddressBook(model, "other name");
        assertTrue(model.getFilteredPersonList().size() == 0);
    }

    @Test
    public void find_appointment_success() throws Exception {
        ScheduleModel model = new ScheduleModelManager();
        model.resetData(TypicalEvents.getTypicalSchedule());

        assertFoundInSchedule(model, TypicalEvents.e1.getId().toString());
        assertTrue(model.getFilteredEventList().size() == 1);
        assertEquals(model.getFilteredEventList().get(0), TypicalEvents.e1);
    }

    @Test
    public void find_invalidParameter() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(FindCommand.MESSAGE_UNEXPECTED_PARAMETER);

        new FindCommand("invalid", "ignored").execute(new AddressBookModelManager(),
                new ScheduleModelManager(),
                new DiagnosisModelManager(),
                new CommandHistory());
    }

    @Test
    public void findDisease_success() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("influenza");
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandResult commandResult = new CommandResult("influenza"
                + FindCommand.THE_FOLLOWING_SYMPTOMS_MATCHING
                + "influenza" + ":\n"
                + "\n"
                + CommandResult.convertListToString(diagnosisModel.getSymptoms(new Disease("influenza"))));
        assertEquals(commandResult, testDiagnosisModel(diagnosisModel, sb.toString()));
    }

    @Test
    public void findDisease_noDisease() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage("sadness" + FindCommand.NO_DISEASE_FOUND);
        testDiagnosisModel(new DiagnosisModelManager(), "sadness");
    }

    /**
     * ensures that string is found in address book
     *
     * @param addressBookModel model
     * @param searchString     search string
     * @throws Exception
     */
    private void assertFoundInAddressBook(AddressBookModel addressBookModel, String searchString)
            throws Exception {

        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        FindCommand cmd = new FindCommand(CMDTYPE_PATIENT, searchString);

        assertEquals(cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory),
                new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        addressBookModel.getFilteredPersonList().size())));
    }

    /**
     * ensures that id is found in events schedule
     *
     * @param scheduleModel model
     * @param searchString  search String
     * @throws Exception
     */
    private void assertFoundInSchedule(ScheduleModel scheduleModel, String searchString)
            throws Exception {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        FindCommand cmd = new FindCommand(CMDTYPE_APPOINTMENT, searchString);
        assertEquals(cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory),
                new CommandResult(String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW,
                        scheduleModel.getFilteredEventList().size())));

    }

    /**
     * Tests diagnosis model
     *
     * @param toAdd string that needs to be added
     * @throws Exception
     */
    private CommandResult testDiagnosisModel(DiagnosisModel diagnosisModel, String toAdd) throws Exception {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        CommandHistory commandHistory = new CommandHistory();

        FindCommand cmd = new FindCommand(CMDTYPE_DISEASE, toAdd);

        return cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);
    }
}
