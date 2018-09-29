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
            return getTomorrowDateTime(currentTime);
            break;
        case "next week":
            return getNextWeekDateTime(currentTime);
            break;
        case "next month":
            return getNextMonthDateTime(currentTime);
            break;
        case "in a few days":
        case "recently":
        case "soon":
            return getNearFutureDateTime(currentTime);
            break;
        default:
            printHelpInfo();     // maybe should be an error implementation
            return currentTime;
    }
}
    private Calendar getTomorrowDateTime(Calendar date) {
        date.add(Calendar.DATE, 1);
        return promptForTime(date, date);
    }

    private Calendar getNextWeekDateTime(Calendar date) {
        // TO-DO
        // get the next week start date to end date

        return promptForTime(startDate, endDate);

    }

    private Calendar getNextMonthDateTime(Calendar date) {
        // TO-DO
        // get the next month start date to end date

        return promptForTime(startDate, endDate);
    }

    private Calendar getNearFutureDateTime(Calendar date) {
        Calendar copy = (Calendar) date.clone();
        date.add(Calendar.DATE, 1);
        copy.add(Calendar.DATE, 7);

        return promptForTime(date, copy);
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






