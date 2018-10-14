package seedu.address.logic.parser;

import java.util.Calendar;
import java.util.List;


import javafx.util.Pair;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.ScheduleEvent;

public class DateTimeParser {

    private ScheduleModel scheduleModel;

    public DateTimeParser (ScheduleModel model) {
        this.scheduleModel = model;
    }

    public Pair<Calendar, Calendar> parseDateTime(String dateInput) {
        Pair<Calendar, Calendar> resultantDateInterval = parseDate(dateInput);
        Pair<Calendar, Calendar> resultantTimeSlot = parseTime(resultantDateInterval);
        return resultantTimeSlot;
    }


    private Pair<Calendar, Calendar> parseDate(String input) {
        Calendar currentTime = Calendar.getInstance();
        Pair<Calendar, Calendar> resultantDateInterval = getResultantDate(currentTime, input);
        return resultantDateInterval;

    }

    private Pair<Calendar, Calendar> parseTime(Pair<Calendar, Calendar> resultantDateInterval) {
        String timeSlot = promptForTimeSlot(resultantDateInterval);
        // TO-DO
        // now do the parsing from the string input to Pair<Calendar, Calendar>
        // the final Pair of Calendar should be ready to be put into a ScheduleEvent;
        return new Pair<Calendar, Calendar>(Calendar.getInstance(), Calendar.getInstance()); // dummy
    }

    private String promptForTimeSlot(Pair<Calendar, Calendar> resultantDateInterval) {
        String availableTime = getAvailableTimeBetween(resultantDateInterval);
        Prompt prompt = new Prompt();
        return prompt.promptForMoreInput(availableTime);
    }

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

        }

        return null;
    }

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
            }
        } else if (dateInput.equals("in a few days")) {
            return getNearFutureDates(currentTime);
        } else {
            return null;
        }

    }

    private Pair<Calendar, Calendar> getSingleDate(Calendar currentDate, int offset) {
        Calendar dateStart = (Calendar) currentDate.clone();
        Calendar dateEnd = (Calendar) currentDate.clone();
        dateStart.add(Calendar.DATE, offset);
        dateEnd.add(Calendar.DATE, offset);
        setDateStartAndEnd(dateStart, dateEnd);
        Pair<Calendar, Calendar> date = new Pair<>(dateStart, dateEnd);
        return date;
    }

    private void setDateStartAndEnd(Calendar start, Calendar end) {
        start.set(Calendar.HOUR, 8);
        start.set(Calendar.MINUTE, 59);
        end.set(Calendar.HOUR, 18);
        end.set(Calendar.MINUTE, 1);
    }

    private Pair<Calendar, Calendar> getWeekDates(Calendar date, int offset) {
        // TO-DO
        // get the next week start date and end date

        Calendar startDate = Calendar.getInstance(); // dummy
        Calendar endDate = Calendar.getInstance(); // dummy

        return promptForTime(startDate, endDate);

    }

    private Pair<Calendar, Calendar> getMonthDates(Calendar date, int offset) {
        // TO-DO
        // get the next month start date and end date

        Calendar startDate = Calendar.getInstance(); // dummy
        Calendar endDate = Calendar.getInstance(); // dummy

        return promptForTime(startDate, endDate);
    }

    private Pair<Calendar, Calendar> getNearFutureDates(Calendar currentDate) {
        Calendar startDate = (Calendar) currentDate.clone();
        Calendar endDate = (Calendar) currentDate.clone();
        startDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.DATE, 7);
        setDateStartAndEnd(startDate, endDate);
        Pair<Calendar, Calendar> dateDuration = new Pair<>(startDate, endDate);
        return dateDuration;
    }

    private Pair<Calendar, Calendar> getDateFromSpecified(String dateTimeInput) {
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






