package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddCommand.MESSAGE_INVALID_PATIENT_FORMAT;
import static seedu.address.logic.commands.AddCommand.MESSAGE_USAGE_PERSON;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DISEASE;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.logic.parser.DiseaseMatcherCliSyntax.PREFIX_DISEASE;
import static seedu.address.logic.parser.DiseaseMatcherCliSyntax.PREFIX_SYMPTOM;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.PersonBuilder.DEFAULT_ADDRESS;
import static seedu.address.testutil.PersonBuilder.DEFAULT_EMAIL;
import static seedu.address.testutil.PersonBuilder.DEFAULT_NAME;
import static seedu.address.testutil.PersonBuilder.DEFAULT_PHONE;
import static seedu.address.testutil.PersonUtil.matchProperties;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;



public class AddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parsePatient_success() throws Exception {

        // normal input
        Person person = new PersonBuilder(ALICE).withTags(VALID_TAG_FRIEND).build();
        Person addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(person));
        assertTrue(matchProperties(person, addedPerson));

        // multiple names - last name accepted
        person = new PersonBuilder(ALICE).build();
        addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB).build())
                + PREFIX_NAME + DEFAULT_NAME);
        assertTrue(matchProperties(person, addedPerson));

        // multiple phones - last phone accepted
        person = new PersonBuilder(ALICE).build();
        addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB).build())
                + PREFIX_PHONE + DEFAULT_PHONE);
        assertTrue(matchProperties(person, addedPerson));

        // multiple emails - last email accepted
        person = new PersonBuilder(ALICE).build();
        addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB).build())
                + PREFIX_EMAIL + DEFAULT_EMAIL);
        assertTrue(matchProperties(person, addedPerson));

        // multiple addresses - last address accepted
        person = new PersonBuilder(ALICE).build();
        addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(new PersonBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).build())
                + PREFIX_ADDRESS + DEFAULT_ADDRESS);
        assertTrue(matchProperties(person, addedPerson));

        // multiple tags - all accepted
        person = new PersonBuilder(ALICE).withTags(VALID_TAG_FRIEND)
                .withTags(VALID_TAG_HUSBAND).build();
        addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(person));
        assertTrue(matchProperties(person, addedPerson));
    }

    @Test
    public void parsePatient_optionalFieldsMissing_success() throws Exception {
        Person person = new PersonBuilder(ALICE).build();
        Person addedPerson = testAddressBookModel(PersonUtil.getPersonDetails(person));
        assertTrue(matchProperties(person, addedPerson));
    }

    @Test
    public void parsePatient_missingCompulsoryFields_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE_PERSON));
        testAddressBookModel(NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY);
    }

    @Test
    public void parsePatient_incorrectNameFormat_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_PATIENT_FORMAT, Name.MESSAGE_NAME_CONSTRAINTS));
        // no & allowed in name
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + "James&" + " ");
        sb.append(PREFIX_PHONE + DEFAULT_PHONE + " ");
        sb.append(PREFIX_EMAIL + DEFAULT_EMAIL + " ");
        sb.append(PREFIX_ADDRESS + DEFAULT_ADDRESS + " ");
        testAddressBookModel(sb.toString());
    }

    @Test
    public void parsePatient_incorrectPhoneFormat_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_PATIENT_FORMAT, Phone.MESSAGE_PHONE_CONSTRAINTS));
        // no a allowed in phone
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + DEFAULT_NAME + " ");
        sb.append(PREFIX_PHONE + "911a" + " ");
        sb.append(PREFIX_EMAIL + DEFAULT_EMAIL + " ");
        sb.append(PREFIX_ADDRESS + DEFAULT_ADDRESS + " ");
        testAddressBookModel(sb.toString());
    }

    @Test
    public void parsePatient_incorrectEmailFormat_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_PATIENT_FORMAT, Email.MESSAGE_EMAIL_CONSTRAINTS));
        // no @ present in email
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + DEFAULT_NAME + " ");
        sb.append(PREFIX_PHONE + DEFAULT_PHONE + " ");
        sb.append(PREFIX_EMAIL + "bob!yahoo" + " ");
        sb.append(PREFIX_ADDRESS + DEFAULT_ADDRESS + " ");
        testAddressBookModel(sb.toString());
    }

    @Test
    public void parsePatient_incorrectAddressFormat_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_PATIENT_FORMAT, Address.MESSAGE_ADDRESS_CONSTRAINTS));
        // empty string not allowed for address
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + DEFAULT_NAME + " ");
        sb.append(PREFIX_PHONE + DEFAULT_PHONE + " ");
        sb.append(PREFIX_EMAIL + DEFAULT_EMAIL + " ");
        sb.append(PREFIX_ADDRESS + "" + " ");
        testAddressBookModel(sb.toString());
    }

    @Test
    public void parsePatient_incorrectTagFormat_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_PATIENT_FORMAT, Tag.MESSAGE_TAG_CONSTRAINTS));
        // * not allowed in tag
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + DEFAULT_NAME + " ");
        sb.append(PREFIX_PHONE + DEFAULT_PHONE + " ");
        sb.append(PREFIX_EMAIL + DEFAULT_EMAIL + " ");
        sb.append(PREFIX_ADDRESS + DEFAULT_ADDRESS + " ");
        sb.append("t/tag*");
        testAddressBookModel(sb.toString());
    }

    @Test
    public void parseDisease_success() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "acnes" + " ");
        sb.append(PREFIX_SYMPTOM + "whitehead" + " ");
        CommandResult commandResult = new CommandResult(AddCommand.NEW_DISEASE
                + "acnes"
                + AddCommand.HAS_BEEN_ADDED_INTO_OUR_RECORD);
        assertEquals(commandResult, testDiagnosisModel(sb.toString()));
        Files.deleteIfExists(Paths.get("datasetForSymptomAndDisease.csv"));
    }

    @Test
    public void parseDisease_noPrefix() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_DISEASE));
        StringBuilder sb = new StringBuilder();
        sb.append("autism" + " ");
        sb.append("lonely" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_noDiseaseValue() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.NO_DISEASE_PARAMETER);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + " ");
        sb.append(PREFIX_SYMPTOM + "lonely" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_multipleDisease() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MULTIPLE_DISEASE_PARAMETER_ERROR);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autism" + " ");
        sb.append(PREFIX_DISEASE + "loneliness" + " ");
        sb.append(PREFIX_SYMPTOM + "lonely" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_diseaseContainsComma() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.ILLEGAL_CHAR_IN_DISEASE_PARAMETER);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autis,m" + " ");
        sb.append(PREFIX_SYMPTOM + "lonely" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_diseaseLengthLimit() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.INVALID_PARAMETER_LENGTH_DISEASE);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autismmmmmmmmmmmmmmmm" + " ");
        sb.append(PREFIX_SYMPTOM + "lonely" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_symptomLengthLimit() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.INVALID_PARAMETER_LENGTH_SYMPTOM);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autism" + " ");
        sb.append(PREFIX_SYMPTOM + "lonelyyyyyyyyyyyyyyyy" + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_noSymptomValue() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.EMPTY_SYMPTOM_ERROR);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autism" + " ");
        sb.append(PREFIX_SYMPTOM + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_symptomContainsComma() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.ILLEGAL_CHAR_IN_SYMPTOM_PARAMETER);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "autism" + " ");
        sb.append(PREFIX_SYMPTOM + "lonely," + " ");
        testDiagnosisModel(sb.toString());
    }

    @Test
    public void parseDisease_duplicateDisease() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_DISEASE);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DISEASE + "migraine disorders" + " ");
        sb.append(PREFIX_SYMPTOM + "vomiting" + " ");
        testDiagnosisModel(sb.toString());
    }

    /**
     * Tests addressbook model
     *
     * @param toAdd add string
     * @return person added
     * @throws Exception
     */
    private Person testAddressBookModel(String toAdd) throws Exception {

        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        AddCommand cmd = new AddCommand(CMDTYPE_PATIENT, toAdd);

        cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);

        // added person should be in index 0
        return addressBookModel.internalGetFromPersonList(unused -> true).get(0);
    }

    /**
     * Tests diagnosis model
     *
     * @param toAdd string that needs to be added
     * @throws Exception
     */
    private CommandResult testDiagnosisModel(String toAdd) throws Exception {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandHistory commandHistory = new CommandHistory();

        AddCommand cmd = new AddCommand(CMDTYPE_DISEASE, toAdd);

        return cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);
    }
}
