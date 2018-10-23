package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Calendar;

import javafx.util.Pair;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;


/**
 * Parses input arguments and creates a new ScheduleEvent object
 */
public class ScheduleCommandParser {

    public static final String MESSAGE_CORRECT_FORMAT = "Expected format: schedule for [name] [date/duration].\n"
            + "Please enter date/duration in natural expressions or in DD/MM/YYYY format.\n"
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
            appointment = generateScheduleEvent(splitString);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_CORRECT_FORMAT));
        }
        return appointment;
    }


    /**
     *
     * @param splitString
     * @return
     */
    private ScheduleEvent generateScheduleEvent(String[] splitString) throws ParseException {
        assert splitString[0].equals("for");
        int dateIndex = -1; // the index at which date/time input starts in the string array
        for (int i = 2; i < splitString.length; i++) { // loop starts at 2 as name is at least one string long
            if (isSomeDateInput(splitString, i)) {
                dateIndex = i;
                break;
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

        Pair<Calendar, Calendar> time = new DateTimeParser(model).parseDateTime(recoveredDateInputBuilder.toString());

        // what do I do with the name????
        //List<String> nameKeywords = Arrays.asList(splitString).subList(1, dateIndex);
        /*
        // recover the name input back to a single string
        StringBuilder recoveredNameInputBuilder = new StringBuilder();
        for (int k = 1; k < dateIndex; k++) {
            recoveredNameInputBuilder.append(splitString[k]);
            recoveredNameInputBuilder.append(" ");
        }
        */
        return null;
    }

    /**
     *
     * @param splitString
     * @param i
     * @return
     */
    private boolean isSomeDateInput(String[] splitString, int i) {
        int noOfWords = splitString.length;
        if (i == noOfWords - 1) { // we are checking starting from the last word
            switch(splitString[i]) {
            case "tomorrow":
            case "tmr":
            case "soon":
            case "recently":
                return true;
            default:
                // do nothing
            }
            if (splitString[i].split("/").length == 3) { // assuming it's DD/MM/YYYY
                return true;
            }
            return false;
        }
        if (i == noOfWords - 2) { // we are checking starting from the second last word
            if (splitString[i].equals("this") || splitString[i].equals("next")) {
                switch (splitString[i + 1]) { // check the last word now
                case "week":
                case "month":
                case "Monday":
                case "Tuesday":
                case "Wednesday":
                case "Thursday":
                case "Friday":
                case "Saturday":
                case "Sunday":
                    return true;
                default:
                    // do nothing
                }
            }
            return false;
        }
        if (i == noOfWords - 3) { // we are checking starting from the third last word
            if (splitString[i].equals("in")) {
                switch (splitString[i + 2]) {
                case "day":
                case "days":
                case "week":
                case "weeks":
                case "month":
                case "months":
                    return true;
                default:
                    // do nothing
                }
            }
            return false;
        }
        if (i == noOfWords - 4) { // we are checking starting from the fourth last word
            String[] theDayAfterTmr = {"the", "day", "after", "tmr"};
            String[] theDayAfterTomorrow = {"the", "day", "after", "tomorrow"};
            String[] inAFewDays = {"in", "a", "few", "days"};
            if (Arrays.equals(splitString, i, noOfWords, theDayAfterTmr, 0, 4)) {
                return true;
            }
            if (Arrays.equals(splitString, i, noOfWords, theDayAfterTomorrow, 0, 4)) {
                return true;
            }
            return Arrays.equals(splitString, i, noOfWords, inAFewDays, 0, 4);
        }
        return false;
    }
}
