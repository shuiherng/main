package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;

/**
 * blabla
 */
public class FindSymptomsCommand extends Command {

    public static final String COMMAND_WORD = "getSymptoms";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all symptoms related to a disease "
            + "(case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        return null;
    }
}
