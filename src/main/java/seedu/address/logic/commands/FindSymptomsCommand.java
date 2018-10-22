package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.ScheduleModel;

/**
 * Finds and lists all related symptoms of a disease in database.
 */
public class FindSymptomsCommand extends Command {

    public static final String COMMAND_WORD = "getSymptoms";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all symptoms related to a disease "
            + "(case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameter: DISEASE\n"
            + "Example: " + COMMAND_WORD + " insufficiency renal";

    public FindSymptomsCommand() {
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel,
                                 ScheduleModel scheduleModel, CommandHistory history) {
        requireNonNull(addressBookModel);
        return null;
    }
}
