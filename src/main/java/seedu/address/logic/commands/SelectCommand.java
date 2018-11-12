package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.SwitchToAppointmentEvent;
import seedu.address.commons.events.ui.SwitchToPatientEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.EventId;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.event.exceptions.ScheduleEventNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the patient or appointment identified by their ID.\n"
            + "Parameters: commandType(patient, appointment), patientID/appointment ID\n"
            + "Example: " + COMMAND_WORD + " patient p5";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Person: %1$s";
    public static final String MESSAGE_SELECT_EVENT_SUCCESS = "Selected Event: %1$s";
    public static final String MESSAGE_INVALID_PERSON_ID = "Invalid Patient ID.";
    public static final String MESSAGE_INVALID_EVENT_ID = "Invalid Appointment ID.";
    public static final String MESSAGE_EVENT_ID_NOT_FOUND = "Appointment ID %1$s not found.";
    public static final String MESSAGE_PERSON_ID_NOT_FOUND = "Patient ID %1$s not found.";
    public static final String MESSAGE_EVENT_ID_NOT_DISPLAYED = "Appointment ID %1$s is not in the panel.";
    public static final String MESSAGE_PERSON_ID_NOT_DISPLAYED = "Patient ID %1$s is not in the panel.";
    public static final String MESSAGE_UNEXPECTED_CMDTYPE = "Unexpected values for cmdType: "
            + "should have been caught in parser.";

    private String cmdType;
    private String target;

    public SelectCommand(String cmdType, String target) {
        this.cmdType = cmdType;
        this.target = target;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);

        if (cmdType.equals(CMDTYPE_PATIENT)) {
            if (!PersonId.isValidId(target)) {
                throw new CommandException(MESSAGE_INVALID_PERSON_ID);
            }
            try {
                Person foundPerson = addressBookModel.getPersonById(new PersonId(target, false));
                EventsCenter.getInstance().post(new JumpToListRequestEvent(Index.fromZeroBased
                        (addressBookModel.getFilteredPersonList().indexOf(foundPerson))));
            } catch (PersonNotFoundException e) {
                throw new CommandException(String.format(MESSAGE_PERSON_ID_NOT_FOUND, target));
            } catch (IndexOutOfBoundsException e) {
                throw new CommandException(String.format(MESSAGE_PERSON_ID_NOT_DISPLAYED, target));
            }
            EventsCenter.getInstance().post(new SwitchToPatientEvent());
            Person foundPerson = addressBookModel.getPersonById(new PersonId(target, false));
            EventsCenter.getInstance().post(new JumpToListRequestEvent(Index.fromZeroBased
                    (addressBookModel.getFilteredPersonList().indexOf(foundPerson))));
            return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, target));
        } else if (cmdType.equals(CMDTYPE_APPOINTMENT)) {
            if (!EventId.isValidId(target)) {
                throw new CommandException(MESSAGE_INVALID_EVENT_ID);
            }
            try {
                ScheduleEvent foundEvent = scheduleModel.getEventById(new EventId(target, false));
                EventsCenter.getInstance().post(new JumpToListRequestEvent(Index.fromZeroBased
                        (scheduleModel.getFilteredEventList().indexOf(foundEvent))));
            } catch (ScheduleEventNotFoundException e) {
                throw new CommandException(String.format(MESSAGE_EVENT_ID_NOT_FOUND, target));
            } catch (IndexOutOfBoundsException e) {
                throw new CommandException(String.format(MESSAGE_EVENT_ID_NOT_DISPLAYED, target));
            }
            EventsCenter.getInstance().post(new SwitchToAppointmentEvent());
            ScheduleEvent foundEvent = scheduleModel.getEventById(new EventId(target, false));
            EventsCenter.getInstance().post(new JumpToListRequestEvent(Index.fromZeroBased
                    (scheduleModel.getFilteredEventList().indexOf(foundEvent))));
            return new CommandResult(String.format(MESSAGE_SELECT_EVENT_SUCCESS, target));
        } else {
            throw new CommandException(MESSAGE_UNEXPECTED_CMDTYPE);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && cmdType.equals(((SelectCommand) other).cmdType)
                && target.equals(((SelectCommand) other).target)); // state check
    }
}
