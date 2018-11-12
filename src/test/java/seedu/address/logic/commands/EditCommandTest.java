package seedu.address.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.EditCommand.MESSAGE_CANNOT_EDIT_DELETED;
import static seedu.address.logic.commands.EditCommand.MESSAGE_INVALID_FORMAT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.ScheduleEventCliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.ScheduleEventCliSyntax.PREFIX_DETAILS;
import static seedu.address.logic.parser.ScheduleEventCliSyntax.PREFIX_PERSON;
import static seedu.address.logic.parser.ScheduleEventCliSyntax.PREFIX_TAGS;
import static seedu.address.testutil.PersonUtil.matchProperties;
import static seedu.address.testutil.ScheduleEventUtil.matchEventProperties;

import java.util.Calendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.Pair;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.ScheduleEventBuilder;

/**
 * Tests the edit command.
 */
public class EditCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // --- start of address book tests ---

    @Test
    public void parse_patient_success() throws Exception {
        // edit name
        Person person = new PersonBuilder().build();
        Person editedPerson = new PersonBuilder().withName(VALID_NAME_AMY).build();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(PREFIX_NAME
                + VALID_NAME_AMY, PREFIX_NAME);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        // edit phone
        person = new PersonBuilder().build();
        editedPerson = new PersonBuilder().withPhone(VALID_PHONE_AMY).build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_PHONE
                + VALID_PHONE_AMY, PREFIX_PHONE);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        // edit email
        person = new PersonBuilder().build();
        editedPerson = new PersonBuilder().withEmail(VALID_EMAIL_AMY).build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_EMAIL
                + VALID_EMAIL_AMY, PREFIX_EMAIL);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        // edit address
        person = new PersonBuilder().build();
        editedPerson = new PersonBuilder().withAddress(VALID_ADDRESS_AMY).build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_ADDRESS
                + VALID_ADDRESS_AMY, PREFIX_ADDRESS);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        // add tag
        person = new PersonBuilder().build();
        editedPerson = new PersonBuilder().withTags(VALID_TAG_FRIEND).build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_TAG + VALID_TAG_FRIEND,
                PREFIX_TAG);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));

        // remove tags
        person = new PersonBuilder().withTags(VALID_TAG_FRIEND).build();
        editedPerson = new PersonBuilder().build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_TAG.toString(), PREFIX_TAG);
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));
    }

    @Test
    public void parsePatient_multipleRepeatedFields_acceptsLast() throws Exception {
        Person person = new PersonBuilder().build();
        Person editedPerson = new PersonBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .build();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(PREFIX_NAME + VALID_NAME_AMY + " "
                + PREFIX_PHONE + VALID_PHONE_AMY + " "
                + PREFIX_NAME + VALID_NAME_BOB + " "
                + PREFIX_PHONE + VALID_PHONE_BOB, PREFIX_NAME, PREFIX_PHONE);
        System.out.println(person);
        System.out.println(argMultimap.getValue(PREFIX_NAME));
        assertTrue(matchProperties(editedPerson, testAddressBookModel(person, argMultimap)));
    }
    @Test
    public void parsePatient_invalidPatientId() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditCommand.MESSAGE_INVALID_PATIENT_ID);
        EditCommand cmd = new EditCommand(CMDTYPE_PATIENT, "invalidID",
                ArgumentTokenizer.tokenize(PREFIX_NAME + VALID_NAME_AMY, PREFIX_NAME));
        cmd.execute(new AddressBookModelManager(),
                new ScheduleModelManager(),
                new DiagnosisModelManager(),
                new CommandHistory());
    }

    @Test
    public void parsePatient_unchanged_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditCommand.MESSAGE_NOT_EDITED);
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize("", PREFIX_NAME));
    }

    @Test
    public void parsePatient_invalidName_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_FORMAT, Name.class.getSimpleName()));
        // no & allowed in name
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize(PREFIX_NAME + "James&", PREFIX_NAME));
    }

    @Test
    public void parsePatient_invalidPhone_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_FORMAT, Phone.class.getSimpleName()));
        // no a allowed in phone number
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize(PREFIX_PHONE + "911a", PREFIX_PHONE));
    }

    @Test
    public void parsePatient_invalidEmail_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_FORMAT, Email.class.getSimpleName()));
        // @ missing from email
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize(PREFIX_EMAIL + "bob!yahoo", PREFIX_EMAIL));
    }

    @Test
    public void parsePatient_invalidAddress_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_FORMAT, Address.class.getSimpleName()));
        // no empty address allowed
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize(PREFIX_ADDRESS + "", PREFIX_ADDRESS));
    }

    @Test
    public void parsePatient_invalidTag_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_FORMAT, Tag.class.getSimpleName()));
        // no * allowed in tag
        testAddressBookModel(new PersonBuilder().build(),
                ArgumentTokenizer.tokenize(PREFIX_TAG + "tag*", PREFIX_TAG));
    }

    @Test
    public void parsePatient_editDeleted_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_CANNOT_EDIT_DELETED);
        Person person = new PersonBuilder().build();
        person.delete();
        // attempt to change person's name
        testAddressBookModel(person, ArgumentTokenizer.tokenize(PREFIX_NAME + VALID_NAME_AMY,
                PREFIX_NAME));
    }

    // --- end of address book tests ---

    // --- start of schedule tests ---

    @Test
    public void parse_appointment_success() throws Exception {
        // edit person
        ScheduleEvent event = new ScheduleEventBuilder().build();
        ScheduleEvent editedEvent = new ScheduleEventBuilder()
                .withPersonId("p229").build();
        ArgumentMultimap argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_PERSON + "p229", PREFIX_PERSON);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

        // edit dateTime
        event = new ScheduleEventBuilder().build();
        Calendar newStart = Calendar.getInstance();
        newStart.set(2019, Calendar.APRIL, 1, 10, 0, 0);
        Calendar newEnd = Calendar.getInstance();
        newEnd.set(2019, Calendar.APRIL, 1, 11, 0, 0);
        editedEvent = new ScheduleEventBuilder()
                .withDurations(new Pair<>(newStart, newEnd)).build();
        argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_DATETIME + "01/04/2019 10:00 - 11:00", PREFIX_DATETIME);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

        // edit details
        event = new ScheduleEventBuilder().build();
        editedEvent = new ScheduleEventBuilder().withDetails("edited details").build();
        argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_DETAILS + "edited details", PREFIX_DETAILS);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

        // add tag
        event = new ScheduleEventBuilder().build();
        editedEvent = new ScheduleEventBuilder().withTags("newtag").build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_TAGS + "newtag", PREFIX_TAGS);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

        // remove tags
        event = new ScheduleEventBuilder().withTags("tag1", "tag2").build();
        editedEvent = new ScheduleEventBuilder().build();
        argMultimap = ArgumentTokenizer.tokenize(PREFIX_TAGS.toString(), PREFIX_TAGS);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));


    }

    @Test
    public void parseAppointment_invalidEventId() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditCommand.MESSAGE_INVALID_EVENT_ID);
        EditCommand cmd = new EditCommand(CMDTYPE_APPOINTMENT, "invalidID",
                ArgumentTokenizer.tokenize(PREFIX_DETAILS + "some details", PREFIX_DETAILS));
        cmd.execute(new AddressBookModelManager(),
                new ScheduleModelManager(),
                new DiagnosisModelManager(),
                new CommandHistory());
    }

    @Test
    public void parseAppointment_invalidDateTimeFormat() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage("Invalid format for date input");

        ScheduleEvent event = new ScheduleEventBuilder().build();
        Calendar newStart = Calendar.getInstance();
        newStart.set(2019, Calendar.APRIL, 1, 10, 0, 0);
        Calendar newEnd = Calendar.getInstance();
        newEnd.set(2019, Calendar.APRIL, 1, 11, 0, 0);
        ScheduleEvent editedEvent = new ScheduleEventBuilder()
                .withDurations(new Pair<>(newStart, newEnd)).build();
        ArgumentMultimap argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_DATETIME + "01/04/2019 11:00", PREFIX_DATETIME);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

    }

    @Test
    public void parseAppointment_invalidTagFormat() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage("Invalid format for tags.");

        ScheduleEvent event = new ScheduleEventBuilder().build();
        ScheduleEvent editedEvent = new ScheduleEventBuilder().build();
        ArgumentMultimap argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_TAGS + "tag*", PREFIX_TAGS);
        assertTrue(matchEventProperties(editedEvent, testScheduleModel(event, argMultimap)));

    }

    @Test
    public void parseAppointment_clashingDateTime() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(DateTimeParser.MESSAGE_INVALID_SLOT,
                DateTimeParser.MESSAGE_SLOT_CLASHING));

        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        Calendar e1Start = Calendar.getInstance();
        e1Start.set(2019, Calendar.APRIL, 1, 10, 0, 0);
        Calendar e1End = Calendar.getInstance();
        e1End.set(2019, Calendar.APRIL, 1, 11, 0, 0);
        ScheduleEvent e1 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e1Start, e1End)).build();

        scheduleModel.addEvent(e1);

        Calendar e2Start = Calendar.getInstance();
        e1Start.set(2019, Calendar.APRIL, 1, 11, 0, 0);
        Calendar e2End = Calendar.getInstance();
        e1End.set(2019, Calendar.APRIL, 1, 12, 0, 0);
        ScheduleEvent e2 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e2Start, e2End)).build();

        scheduleModel.addEvent(e2);

        ArgumentMultimap argMultimap = ArgumentTokenizer
                .tokenize(PREFIX_DATETIME + "01/04/2019 10:30 - 11:30", PREFIX_DATETIME);

        EditCommand cmd = new EditCommand(CMDTYPE_APPOINTMENT, e2.getId().toString(), argMultimap);
        cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);
    }


    // --- end of schedule tests ---

    /**
     * Tests address book model
     * @param original original person
     * @param argMultimap mapping
     * @return edited person
     * @throws Exception
     */
    private Person testAddressBookModel(Person original, ArgumentMultimap argMultimap)
            throws Exception {

        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        addressBookModel.addPerson(original);

        EditCommand cmd = new EditCommand(CMDTYPE_PATIENT, original.getId().toString(), argMultimap);

        cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);

        // added person should be in index 0
        return addressBookModel.internalGetFromPersonList(unused -> true).get(0);
    }

    /**
     * Tests schedule model
     * @param original original event
     * @param argMultimap mapping
     * @return edited event
     * @throws Exception
     */
    private ScheduleEvent testScheduleModel(ScheduleEvent original,
                                            ArgumentMultimap argMultimap) throws Exception {

        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        scheduleModel.addEvent(original);

        EditCommand cmd = new EditCommand(CMDTYPE_APPOINTMENT, original.getId().toString(), argMultimap);

        cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);

        // added scheduleEvent should be in index 0
        return scheduleModel.internalGetFromEventList(unused -> true).get(0);
    }
}
