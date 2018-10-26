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
import seedu.address.model.person.MatchPersonPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;


/**
 * Parses input arguments and creates a new ScheduleEvent object
 */
public class ScheduleEventParser {

    public static final String MESSAGE_SCHEDULE_FORMAT = "Expected format: schedule for [name] [date/duration].\n"
            + "Please enter date/duration in natural expressions or in DD/MM/YYYY format.\n"
            + "Refer to User Guide for the complete list of accepted natural expressions.\n";
    public static final String MESSAGE_PROMPT_NOTES = "Any additional notes?\n";
    public static final String MESSAGE_PROMPT_TIMESLOT = "Please enter a time slot in DD/MM/YYYY hh:mm - hh:mm: \n";
    public static final String MESSAGE_PROMPT_ID = "Please enter the ID of the patient you want to schedule for: \n";
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
        String[] nameInput = generateNameInput(splitInput, dateInputStartAt);
        String dateInput = generateDateInput(splitInput, dateInputStartAt);
        Pair<Calendar> timeSlot = parseDateInput(dateInput);
        PersonId patientId = parseNameInput(nameInput);
        String notes = promptForNotes();
        return new ScheduleEvent(timeSlot, patientId, notes, null);
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
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_SCHEDULE_FORMAT));
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
    private String[] generateNameInput(String[] input, int dateInputStartAt) {
        return Arrays.copyOfRange(input, 1, dateInputStartAt);
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
            String timeSlotInput = new Prompt().promptForMoreInput(MESSAGE_PROMPT_TIMESLOT, availableTimeSlots);
            Pair<Calendar> timeSlot = dateTimeParser.parseTimeSlot(timeSlotInput.trim());
            return timeSlot;
        } catch (ParseException | PromptException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     *
     * @param nameInput
     * @return
     */
    private PersonId parseNameInput(String[] nameInput) throws ParseException {
        try {
            List<Person> nameMatchedPersons = addressBookModel.internalGetFromPersonList
                    (new MatchPersonPredicate(Arrays.asList(nameInput)));
            String personsToDisplay = displayPersonListAsString (nameMatchedPersons);
            String personIdInput = new Prompt().promptForMoreInput(MESSAGE_PROMPT_ID, personsToDisplay);
            List<Person> finalizedPerson = addressBookModel.internalGetFromPersonList
                    (new MatchPersonPredicate(Arrays.asList(personIdInput)));
            if (finalizedPerson.size() == 1) {
                return finalizedPerson.get(0).getId();
            }
            throw new ParseException("AHH");
        } catch (PromptException e) {
            throw new ParseException("AHH");
        }
    }

    /**
     *
     * @return
     * @throws ParseException
     */
    private String promptForNotes() throws ParseException {
        try {
            return new Prompt().promptForMoreInput(MESSAGE_PROMPT_NOTES, "");
        } catch (PromptException e) {
            throw new ParseException("AHH");
        }
    }

    /**
     *
     * @param nameMatchedPersons
     * @return
     */
    private String displayPersonListAsString(List<Person> nameMatchedPersons) {
        StringBuilder personsBuilder = new StringBuilder();
        for (Person person: nameMatchedPersons) {
            personsBuilder.append(person.getId());
            personsBuilder.append(" ");
            personsBuilder.append(person.getName());
            personsBuilder.append("\n");
        }
        return personsBuilder.toString().trim();
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
        return scheduleModel.internalGetFromEventList(scheduleEvent ->
                !scheduleEvent.getDate().getKey().before(dateInterval.getKey())
                        && !scheduleEvent.getDate().getValue().after(dateInterval.getValue()));
    }
}
