package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import seedu.address.commons.util.Pair;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.PromptException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.PersonId;


/**
 * Parses input arguments and creates a new ScheduleEvent object
 */
public class ScheduleEventParser {

    public static final String MESSAGE_CORRECT_FORMAT = "Expected format: schedule for [name] [date/duration].\n"
            + "Please enter date/duration in natural expressions or in DD/MM/YYYY format.\n"
            + "Refer to User Guide for the complete list of accepted natural expressions.\n";
    private ScheduleModel scheduleModel;
    private AddressBookModel addressBookModel;


    public ScheduleEventParser(AddressBookModel addressBookModel, ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
        this.addressBookModel = addressBookModel;
    }

    /**
     * Generates the intended ScheduleEvent from given user input.
     * @param input User input
     * @return The ScheduleEvent intended
     */
    public ScheduleEvent parse(String input) throws ParseException {
        String[] splitInput = input.split("\\s+");
        int dateInputStartAt = breakdownInput(splitInput);
        String nameInput = generateNameInput(splitInput, dateInputStartAt);
        String dateInput = generateDateInput(splitInput, dateInputStartAt);
        Pair<Calendar> timeSlot = parseDateInput(dateInput);
        PersonId patient = parseNameInput(nameInput);
        return null;
    }

    /**
     *
     * @param input
     * @return
     * @throws ParseException
     */
    private int breakdownInput(String[] input) throws ParseException {
        if (input[0].equals("for")) {
            for (int i = 2; i < input.length; i++) { // loop starts at 2 as name is at least one string long
                if (isSomeDateInput(input, i)) {
                    return i;
                }
            }
        }
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_CORRECT_FORMAT));
    }

    /**
     *
     * @param input
     * @param dateInputStartAt
     * @return
     */
    private String generateDateInput(String[] input, int dateInputStartAt) {
        StringBuilder recoveredDateInputBuilder = new StringBuilder();
        for (int j = dateInputStartAt; j < input.length; j++) {
            recoveredDateInputBuilder.append(input[j]);
            recoveredDateInputBuilder.append(" ");
        }
        return recoveredDateInputBuilder.toString();
    }

    /**
     *
     * @param input
     * @param dateInputStartAt
     * @return
     */
    private String generateNameInput(String[] input, int dateInputStartAt) {
        StringBuilder recoveredNameInputBuilder = new StringBuilder();
        for (int k = 1; k < dateInputStartAt; k++) {
            recoveredNameInputBuilder.append(input[k]);
            recoveredNameInputBuilder.append(" ");
        }
        return recoveredNameInputBuilder.toString();
    }

    /**
     *
     * @param dateInput
     * @return
     * @throws ParseException
     */
    private Pair<Calendar> parseDateInput(String dateInput) throws ParseException {
        try {
            Calendar currentTime = Calendar.getInstance();
            DateTimeParser dateTimeParser = new DateTimeParser();
            Pair<Calendar> dateInterval = dateTimeParser.parseDate(dateInput, currentTime);
            List<ScheduleEvent> scheduledAppts = getAppointmentsBetween(dateInterval);
            String availableTimeSlots = dateTimeParser.getAvailableTimeSlotsBetween(scheduledAppts, dateInterval);
            String timeSlotInput = new Prompt().promptForMoreInput(availableTimeSlots);
            Pair<Calendar> timeSlot = dateTimeParser.parseTimeSlot(timeSlotInput.trim());
            return timeSlot;
        } catch (ParseException | PromptException e) {
            throw new ParseException("AHH");
        }
    }

    /**
     *
     * @param nameInput
     * @return
     */
    private PersonId parseNameInput(String nameInput) {
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

    private List<ScheduleEvent> getAppointmentsBetween(Pair<Calendar> dateInterval) {
        scheduleModel.updateFilteredEventList((scheduleEvent) -> {
            return !scheduleEvent.getDate().getKey().before(dateInterval.getKey())
                    && !scheduleEvent.getDate().getValue().after(dateInterval.getValue());
        });
        return scheduleModel.getFilteredEventList();
    }

}
