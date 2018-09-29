package seedu.address.logic.parser;

import java.util.Calendar;

public class DateTimeParser {


    public Calendar parseDateTime (String dateTimeInput) {
        Calendar currentTime = Calendar.getInstance();
        Calendar resultantTime = getResultantTime (currentTime, dateTimeInput);
    }

    private Calendar getResultantTime(Calendar currentTime, String dateTimeInput) {

        switch (dateTimeInput) {
        case "tomorrow":
        case "tmr":
            return getSingleDateTime(currentTime, 1);
        case "the day after tomorrow":
        case "the day after tmr":
        case "in two days":
        case "in 2 days":
            return getSingleDateTime(currentTime, 2);
        case "in three days":
        case "in 3 days":
            return getSingleDateTime(currentTime, 3);
        case "next week":
            return getNextWeekDateTime(currentTime);
        case "next month":
            return getNextMonthDateTime(currentTime);
        case "in a few days":
        case "recently":
        case "soon":
            return getNearFutureDateTime(currentTime);
        default:
            return getDateTimeFromSpecified(dateTimeInput);  // user actually inputs the date (eg. 13/12/2018)
    }
}

    private Calendar getSingleDateTime(Calendar date, int offset) {
        date.add(Calendar.DATE, offset);
        return promptForTime(date, date);
    }

    private Calendar getNextWeekDateTime(Calendar date) {
        // TO-DO
        // get the next week start date and end date

        return promptForTime(startDate, endDate);

    }

    private Calendar getNextMonthDateTime(Calendar date) {
        // TO-DO
        // get the next month start date and end date

        return promptForTime(startDate, endDate);
    }

    private Calendar getNearFutureDateTime(Calendar date) {
        Calendar copy = (Calendar) date.clone();
        date.add(Calendar.DATE, 1);
        copy.add(Calendar.DATE, 7);

        return promptForTime(date, copy);
    }

    private Calendar getDateTimeFromSpecified(String dateTimeInput) {
        // TO-DO
        // check if it is really a valid specified date input
        // if yes, create a Calendar object from the specified date input from user
        return promptForTime(date, date);
    }

    private Calendar promptForTime(Calendar startDate, Calendar endDate) {
        if (startDate.equals(endDate)) {
            printEmptySlots(startDate); // print empty slots on that day
        } else {
            printAvailableDays(startDate, endDate);
            String dayInput = askForDay();
            // process the dayInput into a Calendar object
            printEmptySlots(dayInput);
        }
        String timeInput = askForTime(); // ask user for time input
    }






