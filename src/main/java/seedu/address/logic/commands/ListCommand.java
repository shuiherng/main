package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.ScheduleModel.PREDICATE_SHOW_ALL_SCHEDULE_EVENTS;

import java.util.Arrays;

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

    public static final String MESSAGE_USAGE = COMMAND_WORD + "use 'list patient' or 'list disease' "
            + "to list all persons or diseases with index numbers.\n";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    private final String cmdType;

    public ListCommand(String cmdType) {
        this.cmdType = cmdType;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);

        if (this.cmdType.equals("patient")) {
            addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else if (this.cmdType.equals("appointment")) {
            scheduleModel.updateFilteredEventList(PREDICATE_SHOW_ALL_SCHEDULE_EVENTS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else if (this.cmdType.equals("disease")) {
            Disease[] diseaseList = diagnosisModel.getDiseases();
            String cmdResult = "Found the following disease:\n" + Arrays.toString(diseaseList);
            return new CommandResult(cmdResult);
        } else {
            throw new CommandException("Unexpected Values: Should have been caught in FindCommandParser.");
        }

    }
}
