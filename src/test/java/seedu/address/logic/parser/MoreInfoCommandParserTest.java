package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.MoreInfoCommand;

/**
 * Tests MoreInfoCommandParser.
 */

public class MoreInfoCommandParserTest {

    private MoreInfoCommandParser parser = new MoreInfoCommandParser();

    @Test
    public void parse_nonNumerical_throwsParseException() {
        assertParseFailure(parser, "123abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MoreInfoCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_largeInput_throwsParseException() {
        assertParseFailure(parser, "10000", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MoreInfoCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MoreInfoCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MoreInfoCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsCommand() {
        assertParseSuccess(parser, "3", new MoreInfoCommand("3"));
    }
}
