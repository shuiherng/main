package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.symptom.Disease;

/**
 * Finds and lists all related symptoms of a disease in database.
 */
public class FindSymptomsCommand extends Command {

    public static final String COMMAND_WORD = "getSymptoms";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all symptoms related to a disease "
            + "(case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameter: DISEASE\n"
            + "Example: " + COMMAND_WORD + " insufficiency renal";

    private final Disease disease;

    public FindSymptomsCommand(Disease disease) {
        this.disease = disease;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel,
                                 ScheduleModel scheduleModel, DiagnosisModel diagnosisModel, CommandHistory history) {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(addressBookModel);

        diagnosisModel.getSymptoms(disease);

        return new CommandResult(String.format(Messages.MESSAGE_SYMPTOMS_LISTED_OVERVIEW,
                diagnosisModel.getSymptoms(disease).size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindSymptomsCommand // instanceof handles nulls
                && disease.equals(((FindSymptomsCommand) other).disease)); // state check
    }
}
