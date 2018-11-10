package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DrugSearch;
import seedu.address.model.ScheduleModel;

/**
 * Shows full pharmacological information about any drug from the results of the most recent drug search.
 */

public class MoreInfoCommand extends Command {

    public static final String COMMAND_WORD = "moreinfo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Use \"moreinfo [INDEX]\""
            + " to view extended pharmacological data about the corresponding"
            + " search result (INDEX must be a positive integer)."
            + "\nExample: " + COMMAND_WORD + " 3";

    public static final String NO_PRIOR_SEARCH = "Please carry out a search "
        + "using \"find drug [drugname]\" first.";

    public static final String NO_SUCH_RESULT = "This result is not in the list.";

    private final int index;

    public MoreInfoCommand(String keyindex) {
        this.index = Integer.parseInt(keyindex.trim());
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);
        String cmdResult;

        cmdResult = DrugSearch.moreInfo(index);
        if (cmdResult.equals("Empty.")) {
            throw new CommandException(NO_PRIOR_SEARCH);
        } else if (cmdResult.equals("Not in cache.")) {
            throw new CommandException(NO_SUCH_RESULT);
        }

        return new CommandResult(cmdResult);

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MoreInfoCommand // instanceof handles nulls
                && index == ((MoreInfoCommand) other).index); // state check
    }
}
