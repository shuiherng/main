package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_SYMPTOM;

import java.util.Arrays;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEventMatchesPredicate;
import seedu.address.model.person.MatchPersonPredicate;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "use 'find patient' or 'find symptom' "
            + "to find all persons whose names contain any of "
            + "the specified keywords (case-insensitive) or to find all symptoms related to a disease "
            + "and displays them as a list with index numbers.\n"
            + "Parameters to find persons: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: "
            + COMMAND_WORD
            + " patient"
            + " alice bob charlie\n"
            + "Parameter to find symptoms: DISEASE\n"
            + "Example: "
            + COMMAND_WORD
            + " symptom"
            + " influenza\n";

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

        if (this.cmdType.equals(CMDTYPE_PATIENT)) {
            String[] nameKeywords = searchString.split("\\s+");

            addressBookModel.updateFilteredPersonList(new MatchPersonPredicate(Arrays.asList(nameKeywords)));
            cmdResult = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                    addressBookModel.getFilteredPersonList().size());
        } else if (this.cmdType.equals(CMDTYPE_APPOINTMENT)) {
            scheduleModel.updateFilteredEventList(new ScheduleEventMatchesPredicate(searchString));
            cmdResult = String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW,
                    scheduleModel.getFilteredEventList().size());
        } else if (this.cmdType.equals(CMDTYPE_SYMPTOM)) {
            Disease disease = new Disease(searchString.trim().toLowerCase());
            if (!diagnosisModel.hasDisease(disease)) {
                throw new CommandException("Unexpected Error: disease is not present in our record, "
                        + "please add the disease and its related symptoms into the record");
            }
            Symptom[] symptomList = diagnosisModel.getSymptoms(disease);
            cmdResult = "Found the following symptoms matching the disease:\n" + Arrays.toString(symptomList);
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
