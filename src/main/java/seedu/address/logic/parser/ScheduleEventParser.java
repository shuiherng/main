package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_PATIENT_MATCH_FAIL;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_ID;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_NOTES;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_TAGS;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_TIMESLOT;
import static seedu.address.model.tag.Tag.MESSAGE_INVALID_TAG;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.Pair;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.PromptException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.MatchPersonPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.tag.Tag;


/**
 * Parses input arguments and creates a new ScheduleEvent object
 */
public class ScheduleEventParser {


    public static final String WORD_TOMORROW = "tomorrow";
    public static final String WORD_TMR = "tmr";
    public static final String WORD_SOON = "soon";
    public static final String WORD_RECENTLY = "recently";
    public static final String WORD_WEEK = "week";
    public static final String WORD_MONTH = "month";
    public static final String WORD_MONDAY = "Monday";
    public static final String WORD_TUESDAY = "Tuesday";
    public static final String WORD_WEDNESDAY = "Wednesday";
    public static final String WORD_THURSDAY = "Thursday";
    public static final String WORD_FRIDAY = "Friday";
    public static final String WORD_SATURDAY = "Saturday";
    public static final String WORD_SUNDAY = "Sunday";
    public static final String WORD_DAY = "day";
    public static final String WORD_DAYS = "days";
    public static final String WORD_WEEKS = "weeks";
    public static final String WORD_MONTHS = "months";
    public static final String WORD_THIS = "this";
    public static final String WORD_NEXT = "next";
    public static final String WORD_IN = "in";
    public static final String[] PHRASE_THE_DAY_AFTER_TMR = {"the", "day", "after", "tmr"};
    public static final String[] PHRASE_THE_DAY_AFTER_TOMORROW = {"the", "day", "after", "tomorrow"};
    public static final String[] PHRASE_IN_A_FEW_DAYS = {"in", "a", "few", "days"};
    private static final Logger logger = LogsCenter.getLogger(ScheduleEventParser.class);
    private ScheduleModel scheduleModel;
    private AddressBookModel addressBookModel;


    public ScheduleEventParser(AddressBookModel addressBookModel, ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
        this.addressBookModel = addressBookModel;
    }

    /**
     * Generates the intended ScheduleEvent from given user input.
     * @param input User input.
     * @return The ScheduleEvent intended.
     * @throws ParseException If an error occurs during event parsing.
     */
    public ScheduleEvent parse(String input) throws ParseException {
        requireNonNull(input);
        logger.info("Start ScheduleEvent parsing");
        String[] splitInput = input.split("\\s+");
        int dateInputStartAt = findDateInputStart(splitInput);
        String[] patientInput = generatePatientInput(splitInput, dateInputStartAt);
        String dateInput = generateDateInput(splitInput, dateInputStartAt);
        PersonId patientId = parsePatientInput(patientInput);
        Pair<Calendar> timeSlot = parseDateInput(dateInput);
        Set<Tag> tags = promptForTags();
        String notes = promptForNotes();
        return new ScheduleEvent(timeSlot, patientId, notes, tags);
    }

    /**
     * Searches the input string array to find the index where input for date starts.
     * @param input User input.
     * @return The index at which input for date starts.
     * @throws ParseException If an error occurs during event parsing.
     */
    private int findDateInputStart(String[] input) throws ParseException {
        requireNonNull(input);
        if (input[0].equals("for")) {
            for (int i = 2; i < input.length; i++) { // loop starts at 2 as name is at least one string long
                if (isSomeDateInput(input, i)) {
                    return i;
                }
            }
        }
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));
    }

    /**
     * Generates the input for date by taking the relevant part from the input string array.
     * @param input The user input.
     * @param dateInputStartAt The index at which input for date starts.
     * @return The string containing user input for date.
     */
    private String generateDateInput(String[] input, int dateInputStartAt) {
        requireNonNull(input);
        StringBuilder recoveredDateInputBuilder = new StringBuilder();
        for (int j = dateInputStartAt; j < input.length; j++) {
            recoveredDateInputBuilder.append(input[j]);
            recoveredDateInputBuilder.append(" ");
        }
        return recoveredDateInputBuilder.toString();
    }

    /**
     * Generates the input for patient name or ID by taking the relevant part from the input string array.
     * @param input The user input.
     * @param dateInputStartAt The index at which input for date starts.
     * @return The string array containing user input for patient name or ID.
     */
    private String[] generatePatientInput(String[] input, int dateInputStartAt) {
        requireNonNull(input);
        return Arrays.copyOfRange(input, 1, dateInputStartAt);
    }

    /**
     * Parses the given string input into the intended time slot.
     * @param dateInput User's initial input for date.
     * @return The time slot of the appointment, represented as a Pair of Calendar objects.
     * @throws ParseException If an error occurs during parsing.
     */
    private Pair<Calendar> parseDateInput(String dateInput) throws ParseException {
        requireNonNull(dateInput);
        logger.info("Start time parsing");
        try {
            Calendar currentTime = Calendar.getInstance();
            DateTimeParser dateTimeParser = new DateTimeParser();
            Pair<Calendar> dateInterval = dateTimeParser.parseDate(dateInput, currentTime);
            Pair<Calendar> timeSlot = promptForTimeSlot(dateInterval, dateTimeParser);
            return timeSlot;
        } catch (ParseException | PromptException e) {
            logger.warning("Time parsing failed");
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Prompts the user to choose a time slot for the appointment.
     * The user will be provided with a list of available time slots during the specified range to choose from.
     * @param dateInterval The time range where the appointment is supposed to be in.
     * @param dateTimeParser The parser to be used for time slot parsing.
     * @return The final valid chosen time slot.
     * @throws PromptException If an error occurs during prompt stage.
     * @throws ParseException If an error occurs during parsing.
     */
    private Pair<Calendar> promptForTimeSlot(Pair<Calendar> dateInterval, DateTimeParser dateTimeParser)
            throws PromptException, ParseException {
        requireNonNull(dateInterval);
        requireNonNull(dateTimeParser);
        List<ScheduleEvent> scheduledAppts = getAppointmentsBetween(dateInterval);
        String availableTimeSlots = dateTimeParser.getAvailableTimeSlotsBetween(scheduledAppts, dateInterval);
        String timeSlotInput = new Prompt().promptForMoreInput(MESSAGE_PROMPT_TIMESLOT, availableTimeSlots, true);
        Pair<Calendar> timeSlot = dateTimeParser.parseTimeSlot(timeSlotInput.trim());
        verifyTimeSlotValidity(dateInterval, scheduledAppts, timeSlot);
        return timeSlot;
    }

    /**
     * Verifies if the given time slot is valid.
     * It is valid only when the time slot is within the range specified
     * and the time slot does not clash with any other appointment within the range specified.
     * @param dateInterval The time range specified.
     * @param scheduledAppts The list of already scheduled appointments within the range.
     * @param timeSlot The given time slot.
     * @throws ParseException If an error occurs during parsing, indicating invalid time slot.
     */
    private void verifyTimeSlotValidity(Pair<Calendar> dateInterval, List<ScheduleEvent> scheduledAppts,
                                        Pair<Calendar> timeSlot) throws ParseException {
        requireNonNull(dateInterval);
        requireNonNull(scheduledAppts);
        requireNonNull(timeSlot);
        if (isTimeSlotWithinRange(timeSlot, dateInterval)) {
            for (ScheduleEvent appt: scheduledAppts) {
                if (appt.isClashing(timeSlot)) {
                    throw new ParseException(String.format(DateTimeParser.MESSAGE_INVALID_SLOT,
                            DateTimeParser.MESSAGE_SLOT_CLASHING));
                }
            }
        } else {
            throw new ParseException(String.format(DateTimeParser.MESSAGE_INVALID_SLOT,
                    DateTimeParser.MESSAGE_SLOT_NOT_WITHIN_RANGE));
        }
    }

    /**
     * Parses the given string array input into the intended PatientId.
     * @param patientInput The user input containing patient name or ID.
     * @return The patient ID of the intended patient.
     * @throws ParseException If an error occurs during parsing.
     */
    private PersonId parsePatientInput(String[] patientInput) throws ParseException {
        requireNonNull(patientInput);
        logger.info("Start patient parsing");
        try {
            List<Person> matchedPatients = matchPatients(patientInput);
            if (matchedPatients.isEmpty()) {
                throw new ParseException(MESSAGE_PATIENT_MATCH_FAIL);
            }
            return promptForIntendedPatient(matchedPatients).getId();
        } catch (PromptException | ParseException e) {
            logger.warning("Patient parsing failed");
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Matches a given patient name or ID to a list of patients.
     * @param patientInput The user input for patient name or ID.
     * @return The list of patients that can be matched by this input.
     */
    private List<Person> matchPatients(String[] patientInput) {
        requireNonNull(patientInput);
        return addressBookModel.internalGetFromPersonList
                        (new MatchPersonPredicate(Arrays.asList(patientInput)));
    }

    /**
     * Prompts the user to choose the intended patient to schedule for.
     * @param matchedPatients The list of patients matched by initial input for patient.
     * @return The final matched patients.
     * @throws PromptException If an error occurs during prompt stage.
     * @throws ParseException If an error occurs during parsing.
     */
    private Person promptForIntendedPatient(List<Person> matchedPatients)
            throws PromptException, ParseException {
        assert !matchedPatients.isEmpty();
        Set<PersonId> choosablePersonIds = getChoosablePersonIds(matchedPatients);
        assert !choosablePersonIds.isEmpty();
        while (matchedPatients.size() != 1) {
            String displayablePersons = displayPersonListAsString(matchedPatients);
            String personIdInput = new Prompt().promptForMoreInput(MESSAGE_PROMPT_ID, displayablePersons, true);
            String[] splitString = personIdInput.split("\\s+");
            matchedPatients = matchPatients(splitString);
            if (matchedPatients.isEmpty()) {
                throw new ParseException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            for (Person person: matchedPatients) {
                if (!choosablePersonIds.contains(person.getId())) {
                    throw new ParseException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
            }
        }
        return matchedPatients.get(0);
    }

    /**
     * Gets the choosable set of IDs from a given list of patients.
     * @param persons A list of patients.
     * @return The set of IDs obtained from taking every ID from the list.
     */
    private Set<PersonId> getChoosablePersonIds(List<Person> persons) {
        assert !persons.isEmpty();
        Set<PersonId> choosableIds = new HashSet<>();
        for (Person person: persons) {
            choosableIds.add(person.getId());
        }
        return choosableIds;
    }

    /**
     * Prompts for tags to be attached to the appointment.
     * @return Set of tags.
     * @throws ParseException if an error occurs during parsing.
     */
    private Set<Tag> promptForTags() throws ParseException {
        logger.info("Start prompting for tags");
        try {
            String tags = new Prompt().promptForMoreInput(MESSAGE_PROMPT_TAGS, "", false);
            if (tags.equals("")) {
                return new HashSet<>();
            } else {
                String[] splitString = tags.split("\\s+");
                return ParserUtil.parseTags(Arrays.asList(splitString));
            }
        } catch (PromptException | ParseException e) {
            logger.warning("Tags prompting failed");
            throw new ParseException(String.format(MESSAGE_INVALID_TAG, e.getMessage()));
        }
    }

    /**
     * Prompts for additional notes to be attached to the appointment.
     * @return Notes represented as a String.
     * @throws ParseException if an error occurs during parsing.
     */
    private String promptForNotes() throws ParseException {
        logger.info("Start prompting for additional notes");
        try {
            return new Prompt().promptForMoreInput(MESSAGE_PROMPT_NOTES, "", false);
        } catch (PromptException e) {
            logger.warning("Notes prompting failed");
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Transforms a list of patients to a String representing those patients.
     * @param patientList A list of patients
     * @return The list of patients represented as a String.
     */
    private String displayPersonListAsString(List<Person> patientList) {
        assert !patientList.isEmpty();
        StringBuilder personsBuilder = new StringBuilder();
        for (Person person: patientList) {
            personsBuilder.append(person.getId());
            personsBuilder.append(" ");
            personsBuilder.append(person.getName());
            personsBuilder.append("\n");
        }
        return personsBuilder.toString().trim();
    }



    /**
     * Checks whether a given string array, starting at the given index, is a valid date input.
     * @param splitString The user input represented as a string array.
     * @param index The index at which the check is to start
     */
    private boolean isSomeDateInput(String[] splitString, int index) {
        requireNonNull(splitString);
        int noOfWords = splitString.length;
        if (index == noOfWords - 1) { // we are checking starting from the last word
            switch(splitString[index]) {
            case WORD_TOMORROW:
            case WORD_TMR:
            case WORD_SOON:
            case WORD_RECENTLY:
                return true;
            default:
                // do nothing
            }
            if (splitString[index].split("/").length == 3) { // assume it's DD/MM/YYYY for now
                return true;
            }
            return false;
        }
        if (index == noOfWords - 2) { // we are checking starting from the second last word
            if (splitString[index].equals(WORD_THIS)
                    || splitString[index].equals(WORD_NEXT)) {
                switch (splitString[index + 1]) { // check the last word now
                case WORD_WEEK:
                case WORD_MONTH:
                case WORD_MONDAY:
                case WORD_TUESDAY:
                case WORD_WEDNESDAY:
                case WORD_THURSDAY:
                case WORD_FRIDAY:
                case WORD_SATURDAY:
                case WORD_SUNDAY:
                    return true;
                default:
                    // do nothing
                }
            }
            return false;
        }
        if (index == noOfWords - 3) { // we are checking starting from the third last word
            if (splitString[index].equals(WORD_IN)) {
                switch (splitString[index + 2]) {
                case WORD_DAY:
                case WORD_DAYS:
                case WORD_WEEK:
                case WORD_WEEKS:
                case WORD_MONTH:
                case WORD_MONTHS:
                    return true;
                default:
                    // do nothing
                }
            }
            return false;
        }
        if (index == noOfWords - 4) { // we are checking starting from the fourth last word
            if (Arrays.equals(splitString, index, noOfWords, PHRASE_THE_DAY_AFTER_TMR, 0, 4)) {
                return true;
            }
            if (Arrays.equals(splitString, index, noOfWords, PHRASE_THE_DAY_AFTER_TOMORROW, 0, 4)) {
                return true;
            }
            return Arrays.equals(splitString, index, noOfWords, PHRASE_IN_A_FEW_DAYS, 0, 4);
        }
        return false;
    }

    /**
     * Gets the list of appointments scheduled within the specified range.
     * @param dateInterval The time range from which appointments are to be get.
     * @return The list of appointments scheduled within the specified range.
     */
    private List<ScheduleEvent> getAppointmentsBetween(Pair<Calendar> dateInterval) {
        requireNonNull(dateInterval);
        return scheduleModel.internalGetFromEventList(scheduleEvent ->
                !scheduleEvent.getDate().getKey().before(dateInterval.getKey())
                        && !scheduleEvent.getDate().getValue().after(dateInterval.getValue()));
    }

    /**
     * Checks if the given time slot is within the given range.
     * @param timeSlot The time slot.
     * @param interval The time range.
     */

    private boolean isTimeSlotWithinRange(Pair<Calendar> timeSlot, Pair<Calendar> interval) {
        requireNonNull(timeSlot);
        requireNonNull(interval);
        Calendar dayStart = (Calendar) interval.getKey().clone();
        Calendar dayEnd = (Calendar) dayStart.clone();
        dayEnd.set(Calendar.HOUR_OF_DAY, 18);
        do {
            if (!timeSlot.getKey().before(dayStart) && !timeSlot.getValue().after(dayEnd)) {
                return true;
            } else {
                dayStart.add(Calendar.DATE, 1);
                dayEnd.add(Calendar.DATE, 1);
            }
        } while (!dayEnd.after(interval.getValue()));
        return false;
    }

}
