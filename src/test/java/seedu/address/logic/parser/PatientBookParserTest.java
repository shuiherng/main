package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.testutil.PersonBuilder.DEFAULT_NAME;
import static seedu.address.testutil.ScheduleEventBuilder.SAMPLE_EVENTID;
import static seedu.address.testutil.ScheduleEventBuilder.SAMPLE_VALID_DATETIME_EXPRESSION;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;

import seedu.address.logic.commands.*;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.ScheduleEventBuilder;
import seedu.address.testutil.ScheduleEventUtil;

public class PatientBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final PatientBookParser parser = new PatientBookParser();


    @Test
    public void parseCommand_addPatient() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(CMDTYPE_PATIENT, PersonUtil.getPersonDetails(person)), command);
    }

    @Test
    public void parseCommand_addAppointment() throws Exception {
        String argString = "for " + ScheduleEventBuilder.DEFAULT_PERSON + " "
                + ScheduleEventBuilder.DEFAULT_DATETIME_STRING;
        String cmdString = AddCommand.COMMAND_WORD + " " + CMDTYPE_APPOINTMENT + " " + argString;
        AddCommand command = (AddCommand) parser.parseCommand(cmdString);
        assertEquals(new AddCommand(CMDTYPE_APPOINTMENT, argString), command);
    }


    @Test
    public void parseCommand_clearAppointment() throws Exception {
        String cmdString = ClearCommand.COMMAND_WORD + " " + CMDTYPE_APPOINTMENT;
        ClearCommand command = (ClearCommand) parser.parseCommand(cmdString);
        assertEquals(new ClearCommand(CMDTYPE_APPOINTMENT), command);
    }

    @Test
    public void parseCommand_deletePatient() throws Exception {
        PersonId personId = new PersonBuilder().build().getId();
        DeleteCommand command = (DeleteCommand) parser.parseCommand(DeleteCommand.COMMAND_WORD + " "
                + CMDTYPE_PATIENT + " " + personId.toString());
        assertEquals(new DeleteCommand(CMDTYPE_PATIENT, personId.toString()), command);
    }

    @Test
    public void parseCommand_deleteAppointment() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(DeleteCommand.COMMAND_WORD + " "
                + CMDTYPE_APPOINTMENT + " " + SAMPLE_EVENTID);
        assertEquals(new DeleteCommand(CMDTYPE_PATIENT, SAMPLE_EVENTID), command);
    }

    @Test
    public void parseCommand_findPatient() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(FindCommand.COMMAND_WORD + " "
                + CMDTYPE_PATIENT + " " + DEFAULT_NAME);
        assertEquals(new FindCommand(CMDTYPE_PATIENT, DEFAULT_NAME), command);
    }

    @Test
    public void parseCommand_findAppointment() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(FindCommand.COMMAND_WORD + " "
                + CMDTYPE_APPOINTMENT + " " + SAMPLE_VALID_DATETIME_EXPRESSION);
        assertEquals(new FindCommand(CMDTYPE_APPOINTMENT, SAMPLE_VALID_DATETIME_EXPRESSION), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(
                EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    /*
    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " "
                        + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new MatchPersonPredicate(keywords)), command);
    }
    */

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    /*
    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }
    */

    @Test
    public void parseCommand_clearPatient_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
        parser.parseCommand(ClearCommand.COMMAND_WORD + " " + CMDTYPE_PATIENT);
    }

    @Test
    public void parseCommand_clearAppointmentWithExtraArguments_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
        parser.parseCommand(ClearCommand.COMMAND_WORD + " " + CMDTYPE_APPOINTMENT + " extraArgs");
    }

    @Test
    public void parseCommand_deletePatient_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        PersonId personId = new PersonBuilder().build().getId();
        parser.parseCommand(DeleteCommand.COMMAND_WORD + " "
                + CMDTYPE_PATIENT + " " + personId.toString() + " invalid string");
    }

    @Test
    public void parseCommand_deleteAppointment_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        parser.parseCommand(DeleteCommand.COMMAND_WORD + " "
                + CMDTYPE_APPOINTMENT + " " + SAMPLE_EVENTID + " invalid string");
    }

    @Test
    public void parseCommand_find_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        parser.parseCommand(FindCommand.COMMAND_WORD + " " + "invalid");
    }
    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
