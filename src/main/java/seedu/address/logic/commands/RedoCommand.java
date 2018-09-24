package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;

/**
 * Reverts the {@code addressBookModel}'s address book to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);

        if (!addressBookModel.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        addressBookModel.redoAddressBook();
        addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
