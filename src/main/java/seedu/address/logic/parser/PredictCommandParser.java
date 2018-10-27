package seedu.address.logic.parser;

import seedu.address.logic.commands.PredictCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PredictCommand object
 */
public class PredictCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the PredictCommand
     * and returns an PredictCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public PredictCommand parse(String args) throws ParseException {
        return new PredictCommand(args);
    }
}
