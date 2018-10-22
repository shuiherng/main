package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Calendar;

import javafx.util.Pair;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ScheduleEvent object
 */
public class ScheduleCommandParser {

    public static final String MESSAGE_CORRECT_FORMAT = "Please enter date/duration in natural expressions or "
            + "in DD/MM/YYYY format.\n"
            + "Refer to User Guide for the complete list of accepted natural expressions.\n";
    private ScheduleModel model;


    public ScheduleCommandParser (ScheduleModel model) {
        this.model = model;
    }

    /**
     * Generates the intended ScheduleEvent from given user input.
     * @param input User input
     * @return The ScheduleEvent intended
     */
    public ScheduleEvent parse(String input) throws ParseException {
        String[] splitString = input.split("\\s+");
        ScheduleEvent appointment;
        if (splitString[0].equals("for")) {
            appointment = parseNameFirst(splitString);
        } else {
            appointment = parseDateFirst(splitString);
        }
        return appointment;
    }

    /**
     *
     * @param splitString
     * @return
     */
    private ScheduleEvent parseDateFirst(String[] splitString) {
        return null;
    }

    /**
     *
     * @param splitString
     * @return
     */
    private ScheduleEvent parseNameFirst(String[] splitString) throws ParseException {
        assert splitString[0].equals("for");
        int dateIndex = -1; // the index at which date/time input starts in the string array
        for (int i = 2; i < splitString.length; i++) { // loop starts at 2 as name is at least one string long
            if (isSomeDateInput(splitString, i)) {
                dateIndex = i;
            }
        }
        if (dateIndex == -1) { // no valid input for date found
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_CORRECT_FORMAT));
        }
        // recover the date input back to a single string
        StringBuilder recoveredDateInputBuilder = new StringBuilder();
        for (int j = dateIndex; j < splitString.length; j++) {
            recoveredDateInputBuilder.append(splitString[j]);
            recoveredDateInputBuilder.append(" ");
        }
        // recover the name input back to a single string
        StringBuilder recoveredNameInputBuilder = new StringBuilder();
        for (int k = 1; k < dateIndex; k++) {
            recoveredNameInputBuilder.append(splitString[k]);
            recoveredNameInputBuilder.append(" ");
        }
        Pair<Calendar, Calendar> time = new DateTimeParser(model).parseDateTime(recoveredDateInputBuilder.toString());
        Name patientName = ParserUtil.parseName(recoveredNameInputBuilder.toString());
        return null;
    }

    private boolean isSomeDateInput(String[] splitString, int i) {
        return false;
    }
}
