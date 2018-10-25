package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEventMatchesPredicate;
import seedu.address.model.person.MatchPersonPredicate;
import seedu.address.model.symptom.Symptom;
import seedu.address.model.symptom.DiseaseMatchesPredicate;

import java.util.Arrays;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final String cmdType;
    private final String searchString;

    /* public FindCommand(MatchPersonPredicate predicate) {
        this.predicate = predicate;
    } */
    public FindCommand(String cmdType, String searchString) {
        this.cmdType = cmdType;
        this.searchString = searchString;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);
        String cmdResult;

        if (this.cmdType.equals("patient")) {
            String[] nameKeywords = searchString.split("\\s+");

            addressBookModel.updateFilteredPersonList(new MatchPersonPredicate(Arrays.asList(nameKeywords)));
            cmdResult = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                    addressBookModel.getFilteredPersonList().size());
        } else if (this.cmdType.equals("appointment")) {
            scheduleModel.updateFilteredEventList(new ScheduleEventMatchesPredicate(searchString));
            cmdResult = String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW,
                    scheduleModel.getFilteredEventList().size());
        } else if (this.cmdType.equals("symptoms")) {
            Symptom[] symptomList = diagnosisModel.getDiseases(new DiseaseMatchesPredicate(searchString));
            cmdResult = "Found the following diseases matching the symptoms:\n" + Arrays.toString(symptomList);
        } else {
            throw new CommandException("Unexpected Values: Should have been caught in FindCommandParser.");
        }

        return new CommandResult(cmdResult);

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && cmdType.equals(((FindCommand) other).cmdType)
                && searchString.equals(((FindCommand) other).searchString)); // state check
    }
}
