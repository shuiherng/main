package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_TIMESLOT_FORMAT;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_DAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_DAYS;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_FRIDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_IN;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_MONDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_MONTH;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_MONTHS;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_NEXT;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_RECENTLY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_SATURDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_SOON;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_SUNDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_THIS;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_THURSDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_TMR;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_TOMORROW;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_TUESDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_WEDNESDAY;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_WEEK;
import static seedu.address.logic.parser.ScheduleEventParser.WORD_WEEKS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import seedu.address.commons.util.Pair;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.ScheduleEvent;



/**
 * Parses date time input arguments and creates the corresponding Calendar objects
 * to represent the start time and end time of an appointment.
 * Accepts date time input in natural expression forms and in fixed format.
 */
public class DateTimeParser {

    public static final String MESSAGE_INVALID_SLOT = "Invalid time slot! \n%1$s";
    public static final String MESSAGE_END_BEFORE_START = "The end time of an appointment "
                                                        + "must be after the start time! \n";
    public static final String MESSAGE_SLOT_NOT_WITHIN_RANGE = "Time slot entered is not in the range specified! \n";
    public static final String MESSAGE_SLOT_CLASHING = "Time slot entered clashes with existing appointments! \n";
    public static final String MESSAGE_NO_SLOTS = "No time slots available!\n";
    public static final String MESSAGE_HAVE_SLOTS = "You have time slots available during:\n";
    public static final String MESSAGE_INVALID_INTEGER = "Please enter a valid positive integer number!\n";
    public static final String PHRASE_THE_DAY_AFTER_TOMORROW = "the day after tomorrow";
    public static final String PHRASE_THE_DAY_AFTER_TMR = "the day after tmr";
    public static final String PHRASE_IN_A_FEW_DAYS = "in a few days";
    public static final String MESSAGE_UNEXPECTED_ERROR_DATE_ALR_CHECKED = "Unexpected error, "
                                                                         + "as date validity should have been checked";
    public static final String MESSAGE_UNEXPECTED_ERROR_TIME_ALR_CHECKED = "Unexpected error, "
                                                                         + "as time validity should have been checked";
    public static final SimpleDateFormat SDF_SINGLE_DATE;
    public static final SimpleDateFormat SDF_TIME;

    static {
        SDF_SINGLE_DATE = new SimpleDateFormat("dd/MM/yyyy");
        SDF_TIME = new SimpleDateFormat("kk:mm");
    }

    /**
     * Parses date from input string.
     * @param input Input string for date(s).
     * @return The date interval meant by the string input, represented as a Pair of Calendar objects.
     * @throws ParseException if an error occurs during parsing.
     */
    public Pair<Calendar> parseDate(String input, Calendar currentTime) throws ParseException {
        requireNonNull(input);
        requireNonNull(currentTime);
        zeroOutExtraPrecision(currentTime);
        return getResultantDate(currentTime, input.trim());
    }


    /**
     * Finds the date range intended from a given date/duration input and the current time.
     * @param currentTime Current time.
     * @param dateInput Input string, possibly phrased in natural expressions.
     * @return Date range intended by the input string.
     * @throws ParseException if an error occurs during parsing.
     */
    private Pair<Calendar> getResultantDate(Calendar currentTime, String dateInput) throws ParseException {
        if (dateInput.startsWith(WORD_IN)) {
            return parseIn(currentTime, dateInput);
        } else if (dateInput.startsWith(WORD_THIS) || dateInput.startsWith(WORD_NEXT)) {
            return parseThisOrNext(currentTime, dateInput);
        }
        switch (dateInput) {
        case WORD_TOMORROW:
        case WORD_TMR:
            return getSingleDate(currentTime, 1);
        case PHRASE_THE_DAY_AFTER_TOMORROW:
        case PHRASE_THE_DAY_AFTER_TMR:
            return getSingleDate(currentTime, 2);
        case WORD_RECENTLY:
        case WORD_SOON:
            return getNearFutureDates(currentTime);
        default:
            if (isValidDateFormat(dateInput)) {
                return getDateFromSpecified(dateInput); // user actually inputs the date (eg. 13/12/2018)
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        AddCommand.MESSAGE_USAGE_APPOINTMENT));
            }
        }
    }

    /**
     * Parses "in * day(s)/week(s)/month(s)" expressions.
     * @param currentTime Current time.
     * @param dateInput Input string.
     * @return Date range intended by the input string.
     * @throws ParseException if an error occurs during parsing.
     */
    private Pair<Calendar> parseIn(Calendar currentTime, String dateInput) throws ParseException {
        String[] splitDateInput = dateInput.split("\\s+");
        assert splitDateInput[0].equals(WORD_IN);
        if (Character.isDigit(splitDateInput[1].charAt(0))) {
            int offset;
            try {
                offset = Integer.parseInt(splitDateInput[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INTEGER));
            }
            if (offset == 0) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INTEGER));
            }
            switch (splitDateInput[2]) {
            case WORD_DAYS:
            case WORD_DAY:
                return getSingleDate(currentTime, offset);
            case WORD_WEEKS:
            case WORD_WEEK:
                return getWeekDates(currentTime, offset);
            case WORD_MONTHS:
            case WORD_MONTH:
                return getMonthDates(currentTime, offset);
            default:
            }
        } else if (dateInput.equals(PHRASE_IN_A_FEW_DAYS)) {
            return getNearFutureDates(currentTime);
        }
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));
    }

    /**
     * Parses "this ..." or "next ..." expressions.
     * eg. "this week" "next month" "next Thursday".
     * @param currentTime Current time.
     * @param dateInput Input string.
     * @return Date range intended by the input string.
     * @throws ParseException if an error occurs during parsing.
     */
    private Pair<Calendar> parseThisOrNext(Calendar currentTime, String dateInput) throws ParseException {
        String[] splitDateInput = dateInput.split("\\s+");
        assert splitDateInput[0].equals(WORD_NEXT) || splitDateInput[0].equals(WORD_THIS);
        int offset; // time offset from "now"
        if (splitDateInput[0].equals(WORD_NEXT)) {
            offset = 1;
        } else {
            offset = 0;
        }
        switch (splitDateInput[1]) {
        case WORD_WEEK:
            return getWeekDates(currentTime, offset);
        case WORD_MONTH:
            return getMonthDates(currentTime, offset);
        default:
            // do nothing
        }
        int dayOfWeek = -1;
        switch (splitDateInput[1]) {
        case WORD_MONDAY:
            dayOfWeek = Calendar.MONDAY;
            break;
        case WORD_TUESDAY:
            dayOfWeek = Calendar.TUESDAY;
            break;
        case WORD_WEDNESDAY:
            dayOfWeek = Calendar.WEDNESDAY;
            break;
        case WORD_THURSDAY:
            dayOfWeek = Calendar.THURSDAY;
            break;
        case WORD_FRIDAY:
            dayOfWeek = Calendar.FRIDAY;
            break;
        case WORD_SATURDAY:
            dayOfWeek = Calendar.SATURDAY;
            break;
        case WORD_SUNDAY:
            dayOfWeek = Calendar.SUNDAY;
            break;
        default:
            // do nothing
        }
        if (dayOfWeek == -1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddCommand.MESSAGE_USAGE_APPOINTMENT));
        }
        return getWeekDayDate(currentTime, dayOfWeek, offset);
    }

    /**
     * Gets the date intended from the current time and an offset from the current date.
     * @param currentDate Current time.
     * @param offset Offset from the current date.
     * @return The date intended with working hours applied.
     */
    private Pair<Calendar> getSingleDate(Calendar currentDate, int offset) {
        Calendar dateStart = (Calendar) currentDate.clone();
        Calendar dateEnd = (Calendar) currentDate.clone();
        dateStart.add(Calendar.DATE, offset);
        dateEnd.add(Calendar.DATE, offset);
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Gets the date intended from a given day of the week and offset from the current week.
     * @param currentTime Current time.
     * @param dayOfWeek Day of the week, where 0 represents Monday and 6 represents Sunday.
     * @param offset Offset from the current week, where 0 represents the current week, 1 represents the next week etc.
     * @return The date intended with working hours applied.
     */
    private Pair<Calendar> getWeekDayDate(Calendar currentTime, int dayOfWeek, int offset) {
        assert dayOfWeek != -1;
        Calendar date = (Calendar) currentTime.clone();
        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.add(Calendar.WEEK_OF_YEAR, offset);
        int actualDayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        if (actualDayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SUNDAY) {
            date.add(Calendar.DATE, dayOfWeek - actualDayOfWeek);
        }
        if (actualDayOfWeek == Calendar.SUNDAY && dayOfWeek != Calendar.SUNDAY) {
            date.add(Calendar.DATE, dayOfWeek - 8);
        }
        if (actualDayOfWeek != Calendar.SUNDAY && dayOfWeek == Calendar.SUNDAY) {
            date.add(Calendar.DATE, 8 - actualDayOfWeek);
        }
        Calendar dateStart = (Calendar) date.clone();
        Calendar dateEnd = (Calendar) date.clone();
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Gets the dates for an entire week from the current time and an offset from the current week.
     * @param currentDate Current time.
     * @param offset Offset from the current week.
     * @return The dates intended with working hours applied.
     */
    private Pair<Calendar> getWeekDates(Calendar currentDate, int offset) {
        Calendar weekOffset = (Calendar) currentDate.clone();
        weekOffset.setFirstDayOfWeek(Calendar.MONDAY);
        weekOffset.add(Calendar.WEEK_OF_YEAR, offset);
        Calendar dateStart = (Calendar) weekOffset.clone();
        Calendar dateEnd = (Calendar) weekOffset.clone();
        int dayOfWeek = dateStart.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.SUNDAY) {
            dateStart.add(Calendar.DATE, 2 - dayOfWeek);
            dateEnd.add(Calendar.DATE, 8 - dayOfWeek);
        } else {
            dateStart.add(Calendar.DATE, -6);
        }
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Gets the dates for an entire month from the current time and an offset from the current month.
     * @param currentDate Current time.
     * @param offset Offset from the current month.
     * @return The dates intended with working hours applied.
     */
    private Pair<Calendar> getMonthDates(Calendar currentDate, int offset) {
        Calendar dateStart = (Calendar) currentDate.clone();
        dateStart.add(Calendar.MONTH, offset);
        dateStart.set(Calendar.DAY_OF_MONTH, 1); // set to 1st day of the month
        Calendar dateEnd = (Calendar) dateStart.clone();
        dateEnd.add(Calendar.MONTH, 1); // add one extra month to the end date
        dateEnd.add(Calendar.DAY_OF_MONTH, -1); // subtract one day to get the last day of the previous month
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Gets the dates for the next seven days from the current time.
     * @param currentDate Current time.
     * @return The dates intended with working hours applied.
     */
    private Pair<Calendar> getNearFutureDates(Calendar currentDate) {
        Calendar startDate = (Calendar) currentDate.clone();
        Calendar endDate = (Calendar) currentDate.clone();
        startDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.DATE, 7);
        setDateStartAndEnd(startDate, endDate);
        return new Pair<>(startDate, endDate);
    }

    /**
     * Gets the date from a specified date input in a fixed DD/MM/YYYY format
     * @param dateTimeInput The specified date input in DD/MM/YYYY format
     * @return The date intended with working hours applied.
     * @throws ParseException if an error occurs during parsing.
     */
    private Pair<Calendar> getDateFromSpecified(String dateTimeInput) throws ParseException {
        try {
            DateFormat formatter = SDF_SINGLE_DATE;
            Date date = formatter.parse(dateTimeInput);
            Calendar finalDate = new GregorianCalendar();
            finalDate.setTime(date);
            Calendar dateStart = (Calendar) finalDate.clone();
            Calendar dateEnd = (Calendar) finalDate.clone();
            setDateStartAndEnd(dateStart, dateEnd);
            return new Pair<>(dateStart, dateEnd);
        } catch (java.text.ParseException e) {
            throw new ParseException(MESSAGE_UNEXPECTED_ERROR_DATE_ALR_CHECKED);
        }
    }

    /**
     * Checks if the input string has valid numbers for each field in DD/MM/YYYY format, where
     * year must be a positive integer, month must range from 1 to 12, and day must be a positive integer not greater
     * than the maximum number of days in that month.
     * @param dateInput Input string.
     */
    private boolean isValidDateFormat(String dateInput) {
        String[] splitDateString = dateInput.split("/");
        if (splitDateString.length != 3) {
            return false;
        }
        try {
            int day = Integer.parseInt(splitDateString[0]);
            int month = Integer.parseInt(splitDateString[1]);
            int year = Integer.parseInt(splitDateString[2]);
            if (year < 0) {
                return false;
            }
            if (month <= 0 || month > 12) {
                return false;
            }
            if (day <= 0) {
                return false;
            }
            Calendar testCalendar = Calendar.getInstance();
            testCalendar.set(Calendar.YEAR, year);
            testCalendar.set(Calendar.MONTH, month - 1); // MONTH is zero-based
            int maxDays = testCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (day <= maxDays) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns a list of available time slots between the specified range.
     * @param scheduledAppointments A list of already scheduled appointments within the range.
     * @param dateInterval The time range.
     * @return The list of available time slots represented as a String.
     */
    public String getAvailableTimeSlotsBetween(List<ScheduleEvent> scheduledAppointments, Pair<Calendar> dateInterval) {
        requireNonNull(scheduledAppointments);
        requireNonNull(dateInterval);
        List<Pair<Calendar>> availableSlotList = getAvailableSlotList(scheduledAppointments, dateInterval);
        return slotListToString(availableSlotList);
    }

    /**
     * Gets the available time slots for a given time range.
     * A time period is considered available when the doctor does not have an appointment during it.
     * @param scheduledAppts Already scheduled appointments within the time range.
     * @param dateInterval The time range.
     * @return Available time slots during the time range, represented as a list, unsorted due to implementation.
     */
    private List<Pair<Calendar>> getAvailableSlotList(List<ScheduleEvent> scheduledAppts, Pair<Calendar> dateInterval) {

        List<Pair<Calendar>> availableSlots = new ArrayList<>();
        if (!scheduledAppts.isEmpty()) {
            findFirstAvailableSlot(scheduledAppts, availableSlots);
            for (int i = 0; i < scheduledAppts.size() - 1; i++) {
                Calendar currentEnd = scheduledAppts.get(i).getDate().getValue();
                Calendar nextStart = scheduledAppts.get(i + 1).getDate().getKey();
                findAvailableSlotsBetweenTwoAppts(availableSlots, currentEnd, nextStart);
            }
            findLastAvailableSlot(scheduledAppts, availableSlots);
        }
        findCompletelyAvailableDays(scheduledAppts, dateInterval, availableSlots);
        sortTimeSlots(availableSlots);
        return availableSlots;
    }

    /**
     * Finds and appends the first available time slot before the the first scheduled appointment within that range.
     * This available time slot will be from 9:00 of the day where the first appointment is,
     * to the start of the first appointment.
     * @param scheduledAppts The list of already scheduled appointments within the time range.
     * @param availableSlots The list of available time slots within the time range.
     */
    private void findFirstAvailableSlot(List<ScheduleEvent> scheduledAppts, List<Pair<Calendar>> availableSlots) {
        assert !scheduledAppts.isEmpty();
        requireNonNull(availableSlots);
        Calendar firstScheduleStart = scheduledAppts.get(0).getDate().getKey();
        Calendar firstDayStart = (Calendar) firstScheduleStart.clone();
        firstDayStart.set(Calendar.HOUR_OF_DAY, 9);
        firstDayStart.set(Calendar.MINUTE, 0);
        if (firstScheduleStart.after(firstDayStart)) {
            availableSlots.add(new Pair<>(firstDayStart, firstScheduleStart));
        }
    }

    /**
     * Finds and appends all available time slots between two appointments, given
     * the end of one appointment and the start of the the next appointment.
     * @param availableSlots The list of available time slots within the time range.
     * @param currentEnd The end time of one appointment.
     * @param nextStart The start time of the next appointment.
     */
    private void findAvailableSlotsBetweenTwoAppts(List<Pair<Calendar>> availableSlots,
                                                   Calendar currentEnd, Calendar nextStart) {
        requireNonNull(availableSlots);
        requireNonNull(currentEnd);
        requireNonNull(nextStart);
        if (nextStart.get(Calendar.DATE) != currentEnd.get(Calendar.DATE)) {
            // the next appointment is on a different date already
            // the remaining (if applicable) current day will be free all the way till day end
            Calendar dayEnd = (Calendar) currentEnd.clone();
            dayEnd.set(Calendar.HOUR_OF_DAY, 18);
            dayEnd.set(Calendar.MINUTE, 0);
            if (currentEnd.before(dayEnd)) {
                availableSlots.add(new Pair<>(currentEnd, dayEnd));
            }
            // also, on the day where the next appointment is,
            // the time preceding (if applicable) the start of the next appointment will be free
            Calendar dayStart = (Calendar) nextStart.clone();
            dayStart.set(Calendar.HOUR_OF_DAY, 9);
            dayStart.set(Calendar.MINUTE, 0);
            if (nextStart.after(dayStart)) {
                availableSlots.add(new Pair<>(dayStart, nextStart));
            }
        } else if (currentEnd.before(nextStart)) {
            availableSlots.add(new Pair<>(currentEnd, nextStart));
        }
    }

    /**
     * Finds and appends the last available time slot after the the last scheduled appointment within that range.
     * This available time slot will be from the end of the last appointment,
     * to 18:00 of the day where the last appointment is.
     * @param scheduledAppts The list of already scheduled appointments within the time range.
     * @param availableSlots The list of available time slots within the time range.
     */
    private void findLastAvailableSlot(List<ScheduleEvent> scheduledAppts, List<Pair<Calendar>> availableSlots) {
        assert !scheduledAppts.isEmpty();
        requireNonNull(availableSlots);
        Calendar lastScheduleEnd = scheduledAppts.get(scheduledAppts.size() - 1).getDate().getValue();
        Calendar lastDayEnd = (Calendar) lastScheduleEnd.clone();
        lastDayEnd.set(Calendar.HOUR_OF_DAY, 18);
        lastDayEnd.set(Calendar.MINUTE, 0);
        if (lastScheduleEnd.before(lastDayEnd)) {
            availableSlots.add(new Pair<>(lastScheduleEnd, lastDayEnd));
        }
    }

    /**
     * Finds and appends all available time slots during days that are completely empty within a given range.
     * @param scheduledAppts The list of already scheduled appointments within the time range.
     * @param dateInterval The time range.
     * @param availableSlots The list of available time slots within the time range.
     */
    private void findCompletelyAvailableDays(List<ScheduleEvent> scheduledAppts,
                                             Pair<Calendar> dateInterval, List<Pair<Calendar>> availableSlots) {
        requireNonNull(availableSlots);
        Calendar dayPointer = (Calendar) dateInterval.getKey().clone();
        while (!dayPointer.after(dateInterval.getValue())) {
            boolean isEmptyDay = true;
            if (!scheduledAppts.isEmpty()) {
                for (ScheduleEvent appt: scheduledAppts) {
                    // since the maximum interval is one month, using DAY_OF_YEAR will always be safe
                    if (appt.getDate().getKey().get(Calendar.DAY_OF_YEAR) == dayPointer.get(Calendar.DAY_OF_YEAR)) {
                        isEmptyDay = false;
                        break;
                    }
                }
                if (isEmptyDay) {
                    Calendar emptyDayStart = (Calendar) dayPointer.clone();
                    Calendar emptyDayEnd = (Calendar) dayPointer.clone();
                    setDateStartAndEnd(emptyDayStart, emptyDayEnd);
                    availableSlots.add(new Pair<>(emptyDayStart, emptyDayEnd));
                }
            } else {
                Calendar emptyDayStart = (Calendar) dayPointer.clone();
                Calendar emptyDayEnd = (Calendar) dayPointer.clone();
                setDateStartAndEnd(emptyDayStart, emptyDayEnd);
                availableSlots.add(new Pair<>(emptyDayStart, emptyDayEnd));
            }
            dayPointer.add(Calendar.DATE, 1); // next date
        }
    }

    /**
     * Sorts the given list of time slots, primarily based on the start time of the slot.
     * If two slots have the same start time, their end times and compared.
     * @param timeSlots The list of time slots to sort.
     */
    private void sortTimeSlots(List<Pair<Calendar>> timeSlots) {
        requireNonNull(timeSlots);
        timeSlots.sort(new Comparator<Pair<Calendar>>() {
            @Override
            public int compare(Pair<Calendar> timeSlot1, Pair<Calendar> timeSlot2) {
                if (timeSlot1.getKey().compareTo(timeSlot2.getKey()) == 0) {
                    return timeSlot1.getValue().compareTo(timeSlot2.getValue());
                }
                return timeSlot1.getKey().compareTo(timeSlot2.getKey());
            }
        });
    }

    /**
     * Converts a list representation of slots to a string representation.
     * @param slots List representation of slots.
     * @return String representation of the given list of slots.
     */
    private String slotListToString(List<Pair<Calendar>> slots) {
        requireNonNull(slots);
        if (slots.isEmpty()) {
            return MESSAGE_NO_SLOTS;
        }
        StringBuilder availableTimeBuilder = new StringBuilder();
        availableTimeBuilder.append(MESSAGE_HAVE_SLOTS);
        DateFormat dateFormatter = SDF_SINGLE_DATE;
        DateFormat timeFormatter = SDF_TIME;
        int datePointer = -1;
        for (int j = 0; j < slots.size(); j++) {
            Calendar slotStart = slots.get(j).getKey();
            Calendar slotsEnd = slots.get(j).getValue();
            if (slotStart.get(Calendar.DATE) != datePointer) {
                // a new date
                Date formattableDate = slots.get(j).getKey().getTime();
                String formattedDate = dateFormatter.format(formattableDate);
                availableTimeBuilder.append("\n" + formattedDate + ":\n");
                datePointer = slotStart.get(Calendar.DATE);
            }
            Date formattableStartTime = slotStart.getTime();
            Date formattableEndTime = slotsEnd.getTime();
            String start = timeFormatter.format(formattableStartTime);
            String end = timeFormatter.format(formattableEndTime);
            availableTimeBuilder.append(start + " - " + end + "\n");
        }
        return availableTimeBuilder.toString();
    }

    /**
     * Parses the user input for the final chosen time slot.
     * Assumes user input in DD/MM/YYYY hh:mm - hh:mm format
     * @param timeSlotInput User input for final time slot.
     * @return The final Pair of Calendar objects ready to be stored into a ScheduleEvent object.
     * @throws ParseException if an error occurs during parsing.
     */
    public Pair<Calendar> parseTimeSlot(String timeSlotInput) throws ParseException {
        requireNonNull(timeSlotInput);
        String[] splitString = timeSlotInput.split("\\s+");
        if (splitString.length != 4) { // {"DD/YMM/YYYY", "hh:mm", "-", "hh:mm"}
            throw new ParseException(String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));
        }
        String ddmmyyyy = splitString[0];
        String startTime = splitString[1];
        String endTime = splitString[3];
        if (!isValidDateFormat(ddmmyyyy)) {
            throw new ParseException(String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));
        }
        Pair<Calendar> timeSlot = getDateFromSpecified(ddmmyyyy);
        if (!isValidTimeFormat(startTime) || !isValidTimeFormat(endTime)) {
            throw new ParseException(String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));
        }
        setSlotStartAndEnd(timeSlot, startTime, endTime);
        if (!timeSlot.getKey().before(timeSlot.getValue())) {
            throw new ParseException(String.format(MESSAGE_INVALID_SLOT, MESSAGE_END_BEFORE_START));
        }
        return timeSlot;
    }

    /**
     * Checks if the input string has valid numbers for each field in hh:mm format, where
     * hour must be between 9 and 18 inclusive, and minute must be between 0 and 59 inclusive.
     * @param time Input string.
     */
    private boolean isValidTimeFormat(String time) {
        requireNonNull(time);
        String[] splitString = time.split(":");
        if (splitString.length != 2) { // {"hh", "mm"}
            return false;
        }
        try {
            int hour = Integer.parseInt(splitString[0]);
            int minute = Integer.parseInt(splitString[1]);
            if (hour < 9 || hour > 18) {
                return false;
            }
            if (minute < 0 || minute > 59) {
                return false;
            }
            if (hour == 18 && minute != 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sets the hour and minute fields in the given time slot according to string inputs.
     * @param timeSlot The time slot to be set.
     * @param startTime The start time for the time slot, in hh:mm format.
     * @param endTime The end time for the time slot, in hh:mm format.
     * @throws ParseException if an error occurs during parsing.
     */
    private void setSlotStartAndEnd(Pair<Calendar> timeSlot, String startTime, String endTime) throws ParseException {
        requireNonNull(timeSlot);
        requireNonNull(startTime);
        requireNonNull(endTime);
        String[] startHourMin = startTime.split(":");
        String[] endHourMin = endTime.split(":");
        try {
            timeSlot.getKey().set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHourMin[0]));
            timeSlot.getKey().set(Calendar.MINUTE, Integer.parseInt(startHourMin[1]));
            timeSlot.getValue().set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHourMin[0]));
            timeSlot.getValue().set(Calendar.MINUTE, Integer.parseInt(endHourMin[1]));
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_UNEXPECTED_ERROR_TIME_ALR_CHECKED);
        }
    }

    /**
     * Seconds and milliseconds of the given Calendar object are zeroed out as precision is only required up to minute.
     */
    private void zeroOutExtraPrecision(Calendar calendar) {
        requireNonNull(calendar);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Applies working hours on two given dates.
     * The start date will be set to 9:00 while the end date will be set to 18:00
     * @param start The start time.
     * @param end The end time.
     */
    private void setDateStartAndEnd(Calendar start, Calendar end) {
        requireNonNull(start);
        requireNonNull(end);
        start.set(Calendar.HOUR_OF_DAY, 9);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, 18);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
    }

}






