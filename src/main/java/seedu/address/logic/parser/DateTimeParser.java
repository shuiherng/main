package seedu.address.logic.parser;

import java.util.Calendar;
import java.util.List;

import javafx.util.Pair;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;

// TODO: @kumuwu pls fix the javadoc comments

/**
 * Date Time parser for scheduler commands
 */
public class DateTimeParser {

    private ScheduleModel scheduleModel;

    public DateTimeParser (ScheduleModel model) {
        this.scheduleModel = model;
    }

    /**
     * Generates datetime range from given input.
     * @param dateInput date input
     * @return datetime range
     */
    public Pair<Calendar, Calendar> parseDateTime(String dateInput) {
        Pair<Calendar, Calendar> resultantDateInterval = parseDate(dateInput);
        Pair<Calendar, Calendar> resultantTimeSlot = parseTime(resultantDateInterval);
        return resultantTimeSlot;
    }

    /**
     * Parses date from string.
     * @param input Input string
     * @return Date range
     */
    private Pair<Calendar, Calendar> parseDate(String input) {
        Calendar currentTime = Calendar.getInstance();
        Pair<Calendar, Calendar> resultantDateInterval = getResultantDate(currentTime, input);
        return resultantDateInterval;

    }

    /**
     * Refines the datetime interval and prepares it for insertion into the schedule.
     * @param resultantDateInterval date time input.
     * @return refined
     */
    private Pair<Calendar, Calendar> parseTime(Pair<Calendar, Calendar> resultantDateInterval) {
        String timeSlot = promptForTimeSlot(resultantDateInterval);
        // TO-DO
        // now do the parsing from the string input to Pair<Calendar, Calendar>
        // the final Pair of Calendar should be ready to be put into a ScheduleEvent;
        return new Pair<Calendar, Calendar>(Calendar.getInstance(), Calendar.getInstance()); // dummy
    }

    /**
     * Requests for a more refined time slot input.
     * @param resultantDateInterval date interval
     * @return time slot
     */
    private String promptForTimeSlot(Pair<Calendar, Calendar> resultantDateInterval) {
        String availableTime = getAvailableTimeBetween(resultantDateInterval);
        Prompt prompt = new Prompt();
        return prompt.promptForMoreInput(availableTime);
    }

    /**
     * Gets available time slots for date interval given
     * @param resultantDateInterval date interval to find time slots
     * @return string representing available time slots
     */
    private String getAvailableTimeBetween(Pair<Calendar, Calendar> resultantDateInterval) {
        scheduleModel.updateFilteredEventList((scheduleEvent) -> {
            return scheduleEvent.getDate().getKey().after(resultantDateInterval.getKey())
                    && scheduleEvent.getDate().getValue().before(resultantDateInterval.getValue());
        });
        // after and before here are strictly
        // this probably means I need to set my date start time at 8:59 and end at 18:01
        List<ScheduleEvent> scheduledAppointments = scheduleModel.getFilteredEventList();
        StringBuilder availableTime = new StringBuilder();
        // TO-DO
        // now need to find the complement of scheduledAppointments;
        return availableTime.toString();
    }

    /**
     * Finds a date range which satisfies the input string and current calendar time.
     * @param currentTime current time
     * @param dateInput input string
     * @return date range
     */
    private Pair<Calendar, Calendar> getResultantDate(Calendar currentTime, String dateInput) {

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
            return getDateFromSpecified(dateInput); // user actually inputs the date (eg. 13/12/2018)
        }

    }

    /**
     * Parser for "this or next" (week) style commands
     * @param currentTime current datetime
     * @param dateInput input string
     * @return datetime range
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
     * returns day of the week given the input string
     * @param currentTime
     * @param dayOfWeek
     * @param offset
     * @return datetime range
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
        Pair<Calendar, Calendar> finalDate = new Pair<>(dateStart, dateEnd);
        return finalDate;
    }

    /**
     * parses in * days/weeks/month input
     * @param currentTime current datetime
     * @param dateInput input string
     * @return datetime range
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
     * gets datetime range for a single date
     * @param currentDate current datetime
     * @param offset offset from current date(?)
     * @return datetime range
     */
    private Pair<Calendar, Calendar> getSingleDate(Calendar currentDate, int offset) {
        Calendar dateStart = (Calendar) currentDate.clone();
        Calendar dateEnd = (Calendar) currentDate.clone();
        dateStart.add(Calendar.DATE, offset);
        dateEnd.add(Calendar.DATE, offset);
        setDateStartAndEnd(dateStart, dateEnd);
        Pair<Calendar, Calendar> date = new Pair<>(dateStart, dateEnd);
        return date;
    }

    /**
     * Operating hours (?) (set to static?)
     * @param start start time
     * @param end end time
     */
    private void setDateStartAndEnd(Calendar start, Calendar end) {
        start.set(Calendar.HOUR, 8);
        start.set(Calendar.MINUTE, 59);
        end.set(Calendar.HOUR, 18);
        end.set(Calendar.MINUTE, 1);
    }

    /**
     * get week dates
     * @param date date
     * @param offset offset
     * @return datetime range
     */
    private Pair<Calendar, Calendar> getWeekDates(Calendar date, int offset) {
        // TO-DO
        // get the next week start date and end date

        return null;

    }

    /**
     * get month dates
     * @param date date
     * @param offset offset
     * @return datetime range
     */
    private Pair<Calendar, Calendar> getMonthDates(Calendar date, int offset) {
        // TO-DO
        // get the next month start date and end date

        return null;
    }

    /**
     * get near future dates
     * @param currentDate current datetime
     * @return datetime range
     */
    private Pair<Calendar, Calendar> getNearFutureDates(Calendar currentDate) {
        Calendar startDate = (Calendar) currentDate.clone();
        Calendar endDate = (Calendar) currentDate.clone();
        startDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.DATE, 7);
        setDateStartAndEnd(startDate, endDate);
        Pair<Calendar, Calendar> dateDuration = new Pair<>(startDate, endDate);
        return dateDuration;
    }

    /**
     * get date time from specified input
     * @param dateTimeInput date time input
     * @return datetime range
     */
    private Pair<Calendar, Calendar> getDateFromSpecified(String dateTimeInput) {
        // TO-DO
        // check if it is really a valid specified date input
        // if yes, create a Calendar object from the specified date input from user

        return null;
    }

}






