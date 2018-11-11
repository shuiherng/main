package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DISEASE;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DRUG;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.SwitchToAppointmentEvent;
import seedu.address.commons.events.ui.SwitchToPatientEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DrugSearch;
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

    public static final String MESSAGE_UNEXPECTED_PARAMETER = "Unexpected Values: "
            + "Should have been caught in FindCommandParser.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Use 'find patient' "
            + "to find all patients whose names contains any of the specified "
            + "keywords (case-insensitive)."
            + "\nUse 'find disease' to find all symptoms related to any disease in your database."
            + "\nUse 'find drug' to find all drugs licensed for sale in Singapore matching this name "
            + "(only alphabetical inputs)."
            + "\nResults will be displayed in the form of an indexed list."
            + "\nParameters to find persons: KEYWORD [MORE_KEYWORDS]..."
            + "\nExample: "
            + COMMAND_WORD
            + " patient"
            + " alice bob charlie\n"
            + "Parameter to find disease: DISEASE\n"
            + "Example: "
            + COMMAND_WORD
            + " disease"
            + " influenza\n"
            + "Parameter to find drugs: DRUG\n"
            + "Example: "
            + COMMAND_WORD
            + " drug"
            + " Glycomet\n";

    public static final String UNEXPECTED_ERROR = "Unexpected Error: ";
    public static final String NO_DISEASE_FOUND = " is not present in our record,"
            + " please add this disease and "
            + "its related symptoms into the record"
            + "\n\n"
            + AddCommand.MESSAGE_USAGE_DISEASE;
    public static final String THE_FOLLOWING_SYMPTOMS_MATCHING = " is present in our record. "
            + "Found the following symptoms matching ";
    public static final String DRUG_SEARCH_INITIALIZATION_FAIL = "The drug search database could not be initialized.";
    public static final String DRUG_NOT_FOUND = "No results found. Try again with a different query.";
    public static final String DRUG_TOO_GENERIC = "Your search keyword is too generic. It will lead to hundreds "
            + "of results. Try a more specific keyword instead.";

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
            EventsCenter.getInstance().post(new SwitchToPatientEvent());
            cmdResult = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                    addressBookModel.getFilteredPersonList().size());
        } else if (this.cmdType.equals(CMDTYPE_APPOINTMENT)) {
            scheduleModel.updateFilteredEventList(new ScheduleEventMatchesPredicate(searchString));
            EventsCenter.getInstance().post(new SwitchToAppointmentEvent());
            cmdResult = String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW,
                    scheduleModel.getFilteredEventList().size());
        } else if (this.cmdType.equals(CMDTYPE_DISEASE)) {
            Disease disease = new Disease(searchString.trim().toLowerCase());
            if (!diagnosisModel.hasDisease(disease)) {
                throw new CommandException(disease.toString()
                        + NO_DISEASE_FOUND);
            }
            List<Symptom> symptomList = diagnosisModel.getSymptoms(disease);
            cmdResult = disease.toString() + THE_FOLLOWING_SYMPTOMS_MATCHING
                    + disease.toString() + ":\n"
                    + "\n"
                    + CommandResult.convertListToString(symptomList);
        } else if (this.cmdType.equals(CMDTYPE_DRUG)) {
            String result = DrugSearch.find(searchString.trim().toLowerCase());
            if (result.equals("Too generic.")) {
                throw new CommandException(DRUG_TOO_GENERIC);
            } else if (result.equals("Initialization failed.")) {
                throw new CommandException(UNEXPECTED_ERROR + " " + DRUG_SEARCH_INITIALIZATION_FAIL);
            } else if (result.equals("Not found.")) {
                throw new CommandException(DRUG_NOT_FOUND);
            } else {
                cmdResult = result;
            }
        } else {
            throw new CommandException(MESSAGE_UNEXPECTED_PARAMETER);
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
