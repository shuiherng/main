package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DrugSearch;
import seedu.address.model.ScheduleModel;

import static java.util.Objects.requireNonNull;

/**
 * Shows full pharmacological information about any drug from the results of the most recent drug search.
 */

public class MoreInfoCommand extends Command{

    public static final String COMMAND_WORD = "moreinfo";

    private final int index;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": See more "
            + "information about a drug in the search results.\n"
            + "Example: " +COMMAND_WORD+ " 3";

    public static final String NO_PRIOR_SEARCH = "Please carry out a search "
        + "using \"find drug [drugname]\" first.";

    public static final String NO_SUCH_RESULT = "This result is not in the list. Please look through "
            + "the results and type \"moreinfo [index]\" to see more";

    public MoreInfoCommand(String keyindex) {
        this.index = Integer.parseInt(keyindex);
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);
        String cmdResult;

        cmdResult = DrugSearch.moreInfo(index);
        if(cmdResult.equals("Empty")) {
            throw new CommandException(NO_PRIOR_SEARCH);
        }
        else if (cmdResult.equals("Not in list")) {
            throw new CommandException(NO_SUCH_RESULT);
        }

        return new CommandResult(cmdResult);

    }


}
