package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.DiseaseMatcherCliSyntax.PREFIX_SYMPTOM;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;


/**
 * Predicts a disease given a set of symptoms.
 */
public class PredictCommand extends Command {
    public static final String COMMAND_WORD = "predict";
    public static final String MESSAGE_USAGE = "Expected format for predicting a disease: \n "
            + COMMAND_WORD
            + " " + PREFIX_SYMPTOM + "SYMPTOM...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SYMPTOM + "uncoordination "
            + PREFIX_SYMPTOM + "pleuritic pain "
            + "\n";
    public static final String CANNOT_DETERMINE_THE_IDENTITY_OF_THE_DISEASE_THE_REASON_COULD = "We cannot "
            + "determine the identity of the disease.\nThe reason could";
    public static final String DISEASES_THAT_YOU_MAY_BE_LOOKING_FOR = "The diseases that you may be looking for:\n";
    public static final String ERROR_UNACCEPTABLE_VALUES_SHOULD_HAVE_BEEN_PROMPTED_FOR = "Unexpected Error: "
            + "unacceptable values should have been prompted for.";
    public static final String POSSIBLY_DUE_TO_AN_INVALID_COMMAND_FORMAT_PLEASE_CHECK_YOUR_FORMAT_AGAIN = " be "
            + "possibly due to an invalid command format, please check your format again.\n";
    private final String args;

    public PredictCommand(String args) {
        this.args = args;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SYMPTOM);

        if (!arePrefixesPresent(argMultimap, PREFIX_SYMPTOM)) {
            throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        try {
            Set<Symptom> symptomSet = ParserUtil.parseSymptoms(argMultimap.getAllValues(PREFIX_SYMPTOM));
            List<Disease> diseases = diagnosisModel.predictDisease(symptomSet);
            if (diseases.isEmpty()) {
                throw new CommandException(CANNOT_DETERMINE_THE_IDENTITY_OF_THE_DISEASE_THE_REASON_COULD
                        + POSSIBLY_DUE_TO_AN_INVALID_COMMAND_FORMAT_PLEASE_CHECK_YOUR_FORMAT_AGAIN
                        + "\n"
                        + MESSAGE_USAGE);
            }
            String cmdResult = DISEASES_THAT_YOU_MAY_BE_LOOKING_FOR
                    + CommandResult.convertListToString(diseases);
            return new CommandResult(cmdResult);
        } catch (ParseException e) {
            throw new CommandException(ERROR_UNACCEPTABLE_VALUES_SHOULD_HAVE_BEEN_PROMPTED_FOR, e);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        Stream.of(prefixes).forEach(prefix -> System.out.println(prefix));
        Stream.of(prefixes).forEach(prefix -> System.out.println(argumentMultimap.getValue(prefix)));
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PredictCommand // instanceof handles nulls
                && args.equals(((PredictCommand) other).args));
    }
}
