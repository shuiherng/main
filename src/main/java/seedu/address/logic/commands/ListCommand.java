package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DISEASE;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_EXISTING_PERSONS;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.ScheduleModel.PREDICATE_SHOW_ALL_SCHEDULE_EVENTS;
import static seedu.address.model.ScheduleModel.PREDICATE_SHOW_SCHEDULE_EVENTS;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwitchToPatientEvent;
import seedu.address.commons.events.ui.SwitchToScheduleEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.symptom.Disease;


/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String GET_ALL_WORD = "all";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "use 'list patient' or 'list disease' "
            + "to list all persons or diseases with index numbers.\n";

    public static final String MESSAGE_PERSON_SUCCESS = "Listed all existing persons";
    public static final String MESSAGE_PERSON_ALL_SUCCESS = "Listed all existing persons (including deleted ones)";

    public static final String MESSAGE_APPOINTMENT_SUCCESS = "Listed all appointments";
    public static final String MESSAGE_APPOINTMENT_ALL_SUCCESS = "Listed all appointments (including those in the"
            + "past";

    private final String cmdType;
    private final String args;

    public ListCommand(String cmdType, String args) {
        this.cmdType = cmdType;
        this.args = args.trim();
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);

        if (this.cmdType.equals(CMDTYPE_PATIENT)) {
            if (args.equals(GET_ALL_WORD)) {
                addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                EventsCenter.getInstance().post(new SwitchToPatientEvent());
                return new CommandResult(MESSAGE_PERSON_ALL_SUCCESS);
            } else {
                addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_EXISTING_PERSONS);
                EventsCenter.getInstance().post(new SwitchToPatientEvent());
                return new CommandResult(MESSAGE_PERSON_SUCCESS);
            }
        } else if (this.cmdType.equals(CMDTYPE_APPOINTMENT)) {
            if (args.equals(GET_ALL_WORD)) {
                scheduleModel.updateFilteredEventList(PREDICATE_SHOW_ALL_SCHEDULE_EVENTS);
                EventsCenter.getInstance().post(new SwitchToScheduleEvent());
                return new CommandResult(MESSAGE_APPOINTMENT_ALL_SUCCESS);
            } else {
                scheduleModel.updateFilteredEventList(PREDICATE_SHOW_SCHEDULE_EVENTS);
                EventsCenter.getInstance().post(new SwitchToScheduleEvent());
                return new CommandResult(MESSAGE_APPOINTMENT_SUCCESS);
            }
        } else if (this.cmdType.equals(CMDTYPE_DISEASE)) {
            List<Disease> diseaseList = diagnosisModel.getDiseases();
            String cmdResult = "Found the following disease:\n" + ListCommand.convertListToString(diseaseList);
            return new CommandResult(cmdResult);
        } else {
            throw new CommandException("Unexpected Values: Should have been caught in FindCommandParser.");
        }

    }

    private static String convertListToString(List<Disease> diseaseList) {
        String diseaseListString = "1. ";
        int i;
        for (i = 1; i <= diseaseList.size(); i++) {

            if (i % 5 == 0) {
                diseaseListString = diseaseListString.concat(i + ". " + diseaseList.get(i - 1) + "\n");
                continue;
            }

            diseaseListString = diseaseListString.concat(i + ". " + diseaseList.get(i - 1).toString() + ", ");

        }

        diseaseListString = diseaseListString.substring(3);

        if (diseaseListString.charAt(diseaseListString.length() - 1) == ' ') {
            diseaseListString = diseaseListString.substring(0, diseaseListString.length() - 2);
        }

        diseaseListString = diseaseListString.concat("\n");
        return diseaseListString;
    }
}
