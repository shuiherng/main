package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;

/**
 * Lists all diseases in the patient book.
 */
public class ListDiseasesCommand extends Command {

    public static final String COMMAND_WORD = "listDisease";

    public static final String MESSAGE_SUCCESS = "Listed all diseases";

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel
            scheduleModel, DiagnosisModel diagnosisModel, CommandHistory history) {

        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);
        diagnosisModel.getDiseases();
        return new CommandResult(MESSAGE_SUCCESS);

    }
}
