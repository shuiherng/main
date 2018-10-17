package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;

/*
 * Switch between Patient Mode and Calender Mode
 */
public class ModeCommand extends Command{

    public static final String COMMAND_WORD = "mode";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            +": Switch between the patient mode and the appointment mode.\n"
            +"Parameters: MODE\n"
            +"Example: "+ COMMAND_WORD + " patient";

    public static final String MESSAGE_CANNOT_SWITCH = "You are already in this mode.\n";

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, CommandHistory history) {
        return null;
    }
}
