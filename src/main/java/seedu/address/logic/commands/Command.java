package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param addressBookModel {@code AddressBookModel} which the command should operate on.
     * @param scheduleModel    {@code ScheduleModel} which the command should operate on.
     * @param diagnosisModel   {@code DiagnosisModel} which the command should operate on.
     * @param history          {@code CommandHistory} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(AddressBookModel addressBookModel,
                                          ScheduleModel scheduleModel,
                                          DiagnosisModel diagnosisModel,
                                          CommandHistory history)
            throws CommandException;

}
