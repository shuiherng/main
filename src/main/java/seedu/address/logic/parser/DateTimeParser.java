package seedu.address.logic.parser;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CancellationException;

import javafx.util.Pair;
import seedu.address.model.event.exceptions.CalendarEventNotFoundException;

public class DateTimeParser {


    public Pair<Calendar, Calendar> parseDateTime(String dateTimeInput) {
        Calendar currentTime = Calendar.getInstance();
        Pair<Calendar, Calendar> resultantTime = getResultantTime(currentTime, dateTimeInput);
        return resultantTime;
    }

    private Pair<Calendar, Calendar> getResultantTime(Calendar currentTime, String dateTimeInput) {

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
            return getDateTimeFromSpecified(dateTimeInput); // user actually inputs the date (eg. 13/12/2018)
        }
    }

    private Pair<Calendar, Calendar> getSingleDateTime(Calendar date, int offset) {
        date.add(Calendar.DATE, offset);
        return promptForTime(date, date);
    }

    private Pair<Calendar, Calendar> getNextWeekDateTime(Calendar date) {
        // TO-DO
        // get the next week start date and end date

        Calendar startDate = Calendar.getInstance(); // dummy
        Calendar endDate = Calendar.getInstance(); // dummy

        return promptForTime(startDate, endDate);

    }

    private Pair<Calendar, Calendar> getNextMonthDateTime(Calendar date) {
        // TO-DO
        // get the next month start date and end date

        Calendar startDate = Calendar.getInstance(); // dummy
        Calendar endDate = Calendar.getInstance(); // dummy

        return promptForTime(startDate, endDate);
    }

    private Pair<Calendar, Calendar> getNearFutureDateTime(Calendar date) {
        Calendar copy = (Calendar) date.clone();
        date.add(Calendar.DATE, 1);
        copy.add(Calendar.DATE, 7);

        return promptForTime(date, copy);
    }

    private Pair<Calendar, Calendar> getDateTimeFromSpecified(String dateTimeInput) {
        // TO-DO
        // check if it is really a valid specified date input
        // if yes, create a Calendar object from the specified date input from user

        Calendar date = Calendar.getInstance(); // dummy

        return promptForTime(date, date);
    }

    private Pair<Calendar, Calendar> promptForTime(Calendar startDate, Calendar endDate) {
        Calendar decidedDate;
        if (startDate.equals(endDate)) {
            decidedDate = startDate;
        } else {
            //List<Calendar> availableDays = getAvailableDays(startDate, endDate); // to put back later
            //decidedDate = askForDay(availableDays); // to put back later

        }
        //String timeDurationInput = askForTimeDuration(decidedDate); // to put back later
        //Pair<Calendar, Calendar> finalDateTime = parseTimeDurationInput(timeDurationInput, decidedDate); // to put back later
        Pair<Calendar, Calendar> finalDateTime = new Pair<>(Calendar.getInstance(), Calendar.getInstance()); // dummy
        return finalDateTime;
    }
}






