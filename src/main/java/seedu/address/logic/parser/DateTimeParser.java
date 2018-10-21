package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.util.Pair;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;


/**
 * Parses date time input arguments and creates the corresponding Calendar objects
 * to represent the start time and end time of an appointment.
 * Accepts date time input in natural expression forms and in fixed format.
 */
public class DateTimeParser {

    // there is a need to set the seconds when I write the predicates for adding an appointment
    // ie. startTime must come after 8:59:59

    private static final String MESSAGE_CORRECT_FORMAT = "Please enter date/duration in natural expressions or "
            + "in DD/MM/YYYY format.\n"
            + "Refer to User Guide for the complete list of accepted natural expressions.";
    private ScheduleModel scheduleModel;

    public DateTimeParser (ScheduleModel model) {
        this.scheduleModel = model;
    }

    /**
     * Generates the intended time duration from given user input.
     * @param dateInput User's initial date input in natural expressions or in a fixed format (DD/MM/YYYY).
     * @return The time slot intended by the user, represented by a Pair of Calendar objects.
     */
    public Pair<Calendar, Calendar> parseDateTime(String dateInput) throws ParseException {
        Pair<Calendar, Calendar> resultantDateInterval = parseDate(dateInput);
        Pair<Calendar, Calendar> resultantTimeSlot = parseTime(resultantDateInterval);
        return resultantTimeSlot;
    }

    /**
     * Parses date from input string.
     * @param input Input string for date(s).
     * @return The date interval meant by the string input, represented as a Pair of Calendar objects.
     */
    private Pair<Calendar, Calendar> parseDate(String input) throws ParseException {
        Calendar currentTime = Calendar.getInstance();
        Pair<Calendar, Calendar> resultantDateInterval = getResultantDate(currentTime, input);
        return resultantDateInterval;

    }

    /**
     * Refines the datetime interval into a specific time slot on a day.
     * @param resultantDateInterval Date interval.
     * @return The refined final time slot ready to be inserted into the schedule.
     */
    private Pair<Calendar, Calendar> parseTime(Pair<Calendar, Calendar> resultantDateInterval) {
        String timeSlot = promptForTimeSlot(resultantDateInterval);
        // TO-DO
        // now do the parsing from the string input to Pair<Calendar, Calendar>
        // the final Pair of Calendar should be ready to be put into a ScheduleEvent;
        return null;
    }

    /**
     * Prompts the user for a refined time slot input, by providing a list of available time slots
     * during a given date interval.
     * @param resultantDateInterval Date interval.
     * @return The user's further input for a refined time slot during the date interval.
     */
    private String promptForTimeSlot(Pair<Calendar, Calendar> resultantDateInterval) {
        List<Pair<Calendar, Calendar>> availableTimeList = getAvailableTimeBetween(resultantDateInterval);
        String availableTimeString = slotListToString(availableTimeList);
        Prompt prompt = new Prompt();
        return prompt.promptForMoreInput(availableTimeString);
    }

    /**
     *
     * @param slots
     * @return
     */
    private String slotListToString(List<Pair<Calendar, Calendar>> slots) {

        StringBuilder availableTimeBuilder = new StringBuilder();
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormatter = new SimpleDateFormat("kk:mm");
        int datePointer = -1; // initialize to -1 such that the if block will always be executed in the first iteration
        for (int j = 0; j < slots.size(); j++) {
            Calendar slotStart = slots.get(j).getKey();
            Calendar slotsEnd = slots.get(j).getValue();
            if (slotStart.get(Calendar.DATE) != datePointer) {
                // a new date
                Date formattableDate = slots.get(j).getKey().getTime();
                String formattedDate = dateFormatter.format(formattableDate);
                availableTimeBuilder.append(formattedDate + ": \n");
                datePointer = slotStart.get(Calendar.DATE);
            }
            Date formattableStartTime = slotStart.getTime();
            Date formattableEndTime = slotsEnd.getTime();
            String start = timeFormatter.format(formattableStartTime);
            String end = timeFormatter.format(formattableEndTime);
            availableTimeBuilder.append(start + " to " + end + "\n");
        }
        return availableTimeBuilder.toString();
    }

    /**
     * Gets available time slots for a given date interval.
     * @param resultantDateInterval Date interval.
     * @return Available time slots during the date interval, represented as a list.
     */
    private List<Pair<Calendar, Calendar>> getAvailableTimeBetween(Pair<Calendar, Calendar> resultantDateInterval) {
        scheduleModel.updateFilteredEventList((scheduleEvent) -> {
            return scheduleEvent.getDate().getKey().after(resultantDateInterval.getKey())
                    && scheduleEvent.getDate().getValue().before(resultantDateInterval.getValue());
        });
        List<ScheduleEvent> scheduledAppointments = scheduleModel.getFilteredEventList();
        List<Pair<Calendar, Calendar>> emptySlots = new ArrayList<>();
        Calendar firstScheduleStart = scheduledAppointments.get(0).getDate().getKey();
        Calendar dayStartOfficially = (Calendar) resultantDateInterval.getKey().clone(); // day starts at 8:59
        dayStartOfficially.add(Calendar.MINUTE, 1); // now day starts at 9:00
        if (firstScheduleStart.after(dayStartOfficially)) {
            emptySlots.add(new Pair<>(dayStartOfficially, firstScheduleStart));
        }
        // wrap around across day!
        for (int i = 0; i < scheduledAppointments.size() - 1; i++) {
            Calendar currentEnd = scheduledAppointments.get(i).getDate().getValue();
            Calendar nextStart = scheduledAppointments.get(i + 1).getDate().getKey();
            if (currentEnd.before(nextStart)) {
                emptySlots.add(new Pair<>(currentEnd, nextStart));
            }
        }
        Calendar lastScheduleEnd = scheduledAppointments.get(scheduledAppointments.size() - 1).getDate().getValue();
        Calendar dayEndOfficially = (Calendar) resultantDateInterval.getValue().clone(); // day ends at 18:01
        dayEndOfficially.add(Calendar.MINUTE, -1); // now day ends at 18:00
        if (lastScheduleEnd.before(dayEndOfficially)) {
            emptySlots.add(new Pair<>(lastScheduleEnd, dayEndOfficially));
        }
        return emptySlots;

    }

    /**
     * Finds the date range intended from a given date/duration input and the current time.
     * @param currentTime Current time.
     * @param dateInput Input string, possibly phrased in natural expressions.
     * @return Date range intended by the input string.
     */
    private Pair<Calendar, Calendar> getResultantDate(Calendar currentTime, String dateInput) throws ParseException {

        if (dateInput.startsWith("in")) {
            return parseIn(currentTime, dateInput);
        } else if (dateInput.startsWith("this") || dateInput.startsWith("next")) {
            return parseThisOrNext(currentTime, dateInput);
        }
        switch (dateInput) {
        case "tomorrow":
        case "tmr":
            return getSingleDate(currentTime, 1);
        case "the day after tomorrow":
        case "the day after tmr":
            return getSingleDate(currentTime, 2);
        case "recently":
        case "soon":
            return getNearFutureDates(currentTime);
        default:
            if (isValidDateFormat(dateInput)) {
                return getDateFromSpecified(dateInput); // user actually inputs the date (eg. 13/12/2018)
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_CORRECT_FORMAT));
            }
        }

    }


    /**
     * Parser "this ..." or "next ..." commands.
     * eg. "this week" "next month" "next Thur".
     * @param currentTime Current time.
     * @param dateInput Input string.
     * @return Date range intended by the input string.
     */
    private Pair<Calendar, Calendar> parseThisOrNext(Calendar currentTime, String dateInput) {
        String[] splitString = dateInput.split("\\s+");
        assert splitString[0].equals("next") || splitString[0].equals("in");
        switch (splitString[1]) {
        case "week":
            if (splitString[0].equals("next")) {
                return getWeekDates(currentTime, 1);
            } else {
                return getWeekDates(currentTime, 0);
            }
        case "month":
            if (splitString[0].equals("next")) {
                return getMonthDates(currentTime, 1);
            } else {
                return getMonthDates(currentTime, 0);
            }
        default:
            // do nothing
        }
        int dayOfWeek = -1; // requires a check somewhere
        if (splitString[1].contains("Mon")) {
            dayOfWeek = 0;
        } else if (splitString[1].contains("Tue")) {
            dayOfWeek = 1;
        } else if (splitString[1].contains("Wed")) {
            dayOfWeek = 3;
        } else if (splitString[1].contains("Thu")) {
            dayOfWeek = 4;
        } else if (splitString[1].contains("Fri")) {
            dayOfWeek = 5;
        } else if (splitString[1].contains("Sat")) {
            dayOfWeek = 6;
        } else if (splitString[1].contains("Sun")) {
            dayOfWeek = 7;
        }
        if (splitString[0].equals("next")) {
            return getWeekDayDate(currentTime, dayOfWeek, 1);
        } else if (splitString[0].equals("this")) {
            return getWeekDayDate(currentTime, dayOfWeek, 0);
        }
        return null; // need to handle
    }

    /**
     * Gets the date from a given day of the week and offset from the current week.
     * @param currentTime Current time.
     * @param dayOfWeek Day of the week, where 0 represents Monday and 6 represents Sunday.
     * @param offset Offset from the current week, where 0 represents the current weeek, 1 represents the next week etc.
     * @return The date intended with working hours applied.
     */
    private Pair<Calendar, Calendar> getWeekDayDate(Calendar currentTime, int dayOfWeek, int offset) {
        Calendar date = (Calendar) currentTime.clone();
        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date.add(Calendar.WEEK_OF_YEAR, offset); // may need to consider the wrapping around at the year boundary
        date.add(Calendar.DATE, dayOfWeek);
        Calendar dateStart = (Calendar) date.clone();
        Calendar dateEnd = (Calendar) date.clone();
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Parses "in * day(s)/week(s)/month(s)" commands.
     * @param currentTime Current time.
     * @param dateInput Input string.
     * @return Date range intended by the input string.
     */
    private Pair<Calendar, Calendar> parseIn(Calendar currentTime, String dateInput) {
        String[] splitString = dateInput.split("\\s+");
        assert splitString[0].equals("in");
        if (Character.isDigit(splitString[1].charAt(0))) {
            int offset = Integer.parseInt(splitString[1]);
            switch (splitString[2]) {
            case "days":
            case "day":
                return getSingleDate(currentTime, offset);
            case "weeks":
            case "week":
                return getWeekDates(currentTime, offset);
            case "months":
            case "month":
                return getMonthDates(currentTime, offset);
            default:
            }
        } else if (dateInput.equals("in a few days")) {
            return getNearFutureDates(currentTime);
        }
        return null;
    }

    /**
     * Gets the date from the current time and offset from the current date.
     * @param currentDate Current time.
     * @param offset Offset from the current date.
     * @return The date intended with working hours applied.
     */
    private Pair<Calendar, Calendar> getSingleDate(Calendar currentDate, int offset) {
        Calendar dateStart = (Calendar) currentDate.clone();
        Calendar dateEnd = (Calendar) currentDate.clone();
        dateStart.add(Calendar.DATE, offset);
        dateEnd.add(Calendar.DATE, offset);
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Applies working hours on two given dates.
     * The start date will be set to 8:59 while the end date will be set to 18:01
     * @param start The start time.
     * @param end The end time.
     */
    private void setDateStartAndEnd(Calendar start, Calendar end) {
        start.set(Calendar.HOUR, 8);
        start.set(Calendar.MINUTE, 59);
        end.set(Calendar.HOUR, 18);
        end.set(Calendar.MINUTE, 1);
    }

    /**
     * Gets the dates for an entire week from the current time and an offset from the current week.
     * @param currentDate Current time.
     * @param offset Offset from the current week.
     * @return The dates intended with working hours applied.
     */
    private Pair<Calendar, Calendar> getWeekDates(Calendar currentDate, int offset) {
        Calendar weekOffset = (Calendar) currentDate.clone();
        weekOffset.setFirstDayOfWeek(Calendar.MONDAY);
        weekOffset.add(Calendar.WEEK_OF_YEAR, offset);
        Calendar dateStart = (Calendar) weekOffset.clone();
        Calendar dateEnd = (Calendar) weekOffset.clone();
        dateStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dateStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setDateStartAndEnd(dateStart, dateEnd);
        return new Pair<>(dateStart, dateEnd);
    }

    /**
     * Gets the dates for an entire month from the current time and an offset from the current month.
     * @param currentDate Current time.
     * @param offset Offset from the current month.
     * @return The dates intended with working hours applied.
     */
    private Pair<Calendar, Calendar> getMonthDates(Calendar currentDate, int offset) {
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
    private Pair<Calendar, Calendar> getNearFutureDates(Calendar currentDate) {
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
     */
    private Pair<Calendar, Calendar> getDateFromSpecified(String dateTimeInput) throws ParseException {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(dateTimeInput);
            Calendar finalDate = new GregorianCalendar();
            finalDate.setTime(date);
            Calendar dateStart = (Calendar) finalDate.clone();
            Calendar dateEnd = (Calendar) finalDate.clone();
            setDateStartAndEnd(dateStart, dateEnd);
            return new Pair<>(dateStart, dateEnd);
        } catch (java.text.ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_CORRECT_FORMAT));
        }
    }

    /**
     * Checks if the input string has valid numbers for each field in DD/MM/YYYY format, where
     * year must be a positive integer, month must range from 1 to 12, and day must be a positive integer not greater
     * than the maximum number of days in that month.
     * @param dateInput Input string.
     */
    private boolean isValidDateFormat(String dateInput) {
        String[] splitString = dateInput.split("/");
        if (splitString.length != 3) {
            return false;
        }
        try {
            int day = Integer.parseInt(splitString[0]);
            int month = Integer.parseInt(splitString[1]);
            int year = Integer.parseInt(splitString[2]);
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
            if (day > maxDays) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}






