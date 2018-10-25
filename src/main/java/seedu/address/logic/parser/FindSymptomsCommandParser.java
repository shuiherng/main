package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindSymptomsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.symptom.Disease;


/**
 * Parses input arguments and creates a new FindSymptomsCommand object
 */
public class FindSymptomsCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the FindSymptomsCommand
     * and returns an FindSymptomsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindSymptomsCommand parse(String disease) throws ParseException {
        String trimmedDisease = disease.trim();
        if (trimmedDisease.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindSymptomsCommand.MESSAGE_USAGE));
        }
        return new FindSymptomsCommand(new Disease(trimmedDisease));
    }

}
