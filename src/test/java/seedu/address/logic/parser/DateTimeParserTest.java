
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.util.Pair;
import seedu.address.logic.parser.exceptions.ParseException;


public class DateTimeParserTest {

    private static final String VALID_TOMORROW = "tomorrow";
    private static final String VALID_THE_DAY_AFTER_TOMORROW = "the day after tomorrow";
    private static final String VALID_IN_SOME_SMALL_DAYS = "in 5 days";
    private static final String VALID_IN_SOME_BIG_DAYS = "in 365 days";
    private static final String VALID_IN_SOME_SMALL_WEEKS = "in 3 weeks";
    private static final String VALID_IN_SOME_BIG_WEEKS = "in 50 weeks";
    private static final String VALID_IN_SOME_SMALL_MONTHS = "in 5 months";
    private static final String VALID_IN_SOME_BIG_MONTHS = "in 99 months";
    private static final String VALID_THIS_WEEKDAY = "this Friday";
    private static final String VALID_NEXT_WEEKDAY = "next Monday";
    private static final String VALID_THIS_SUNDAY = "this Sunday";
    private static final String VALID_NEXT_SUNDAY = "next Sunday";
    private static final String VALID_THIS_WEEK = "this week";
    private static final String VALID_NEXT_WEEK = "next week";
    private static final String VALID_THIS_MONTH = "this month";
    private static final String VALID_NEXT_MONTH = "next month";
    private static final String VALID_SOON = "soon";
    private static final String VALID_RECENTLY = "recently";
    private static final String VALID_IN_A_FEW_DAYS = "in a few days";
    private static final String VALID_SPECIFIED = "13/12/2018";

    private static final String INVALID_SPECIFIED_FORMAT = "13.12.2018";
    private static final String INVALID_SPECIFIED_DAY_NUMBER = "33/12/2018";
    private static final String INVALID_SPECIFIEDadd_MONTH_NUMBER = "13/13/2018";
    private static final String INVALID_NON_INTEGER_NUMBER = "in 0.8 days";
    private static final String INVALID_BIG_INTEGER_NUMBER = "in 1000000000000000000 months";
    private static final String INVALID_CAPITALIZED = "neXt Month";
    private static final String INVALID_RANDOM = "elephant";

    private static final String REFINED_VALID_TIME_SLOT = "13/12/2018 9:30 - 10:30";
    private static final String REFINED_INVALID_CLASHING = "13/12/2018 10:00 - 10:30";
    private static final String REFINED_INVALID_TOO_EARLY = "13/12/2018 8:30 - 10:30";
    private static final String REFINED_INVALID_T00_LATE = "13/12/2018 19:30 - 23:30";
    private static final String REFINED_INVALID_END_EARLIER_THAN_START = "13/12/2018 10:30 - 9:30";
    private static final String REFINED_INVALID_FORMAT_TIME_SLOT = "13.12.2018 10:30 - 9:30";
    private static final String REFINED_INVALID_FORMAT_WRONG_SEQUENCE_TIME_SLOT = "10:30 - 9:30 13.12.2018";

    private DateTimeParser parser = new DateTimeParser();
    private Calendar actualCurrentTime;
    private Calendar dummyCurrentTime;
    private Calendar dummySundayTime;
    private Calendar expectedCalendarStart;
    private Calendar expectedCalendarEnd;

    @Before
    public void initializeCalendars() {
        actualCurrentTime = Calendar.getInstance();
        //actualCurrentTime.setFirstDayOfWeek(Calendar.MONDAY);
        dummyCurrentTime = (Calendar) actualCurrentTime.clone();
        dummySundayTime = (Calendar) actualCurrentTime.clone();
        dummyCurrentTime.set(2018, 10, 16, 16, 13, 20);
        dummySundayTime.set(2018, 10, 18, 16, 13, 20);
        expectedCalendarStart = (Calendar) actualCurrentTime.clone();
        expectedCalendarEnd = (Calendar) actualCurrentTime.clone();
        zeroOutMilliseconds(expectedCalendarStart, expectedCalendarEnd);
    }

    @Test
    public void parseDate_validInput_noWrapAround() throws ParseException {
        // testing "tomorrow"
        expectedCalendarStart.set(2018, 10, 17, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 17, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd), parser.parseDate(VALID_TOMORROW,
                dummyCurrentTime));

        // testing "the day after tomorrow"
        expectedCalendarStart.set(2018, 10, 18, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 18, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THE_DAY_AFTER_TOMORROW, dummyCurrentTime));

        // testing "this Friday"
        expectedCalendarStart.set(2018, 10, 16, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 16, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_WEEKDAY, dummyCurrentTime));

        // testing "this Friday" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 16, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 16, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_WEEKDAY, dummySundayTime));

        // testing "this Sunday"
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 18, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 18, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_SUNDAY, dummyCurrentTime));

        // testing "this Sunday" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 18, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 18, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_SUNDAY, dummySundayTime));

        // testing "this week"
        expectedCalendarStart.set(2018, 10, 12, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 18, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_WEEK, dummyCurrentTime));

        // testing "this week" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 12, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 18, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_WEEK, dummySundayTime));

        // testing "this month"
        expectedCalendarStart.set(2018, 10, 1, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 30, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_THIS_MONTH, dummyCurrentTime));

        // testing "13/12/2018"
        expectedCalendarStart.set(2018, 11, 13, 9, 0, 0);
        expectedCalendarEnd.set(2018, 11, 13, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_SPECIFIED, dummyCurrentTime));
    }

    @Test
    public void parseDate_validInput_wrapAroundWeek() throws ParseException {
        // testing "in 5 days"
        expectedCalendarStart.set(2018, 10, 21, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 21, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd), parser.parseDate(VALID_IN_SOME_SMALL_DAYS,
                dummyCurrentTime));

        // testing "next Monday"
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 19, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_WEEKDAY, dummyCurrentTime));

        // testing "next Monday" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 19, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_WEEKDAY, dummySundayTime));

        // testing "next Sunday"
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 25, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_SUNDAY, dummyCurrentTime));

        // testing "next Sunday" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 25, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_SUNDAY, dummySundayTime));

        // testing "next week"
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_WEEK, dummyCurrentTime));

        // testing "next week" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_WEEK, dummySundayTime));

        // testing "soon"
        expectedCalendarStart.set(2018, 10, 17, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 23, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_SOON, dummyCurrentTime));

        // testing "recently"
        expectedCalendarStart.set(2018, 10, 17, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 23, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_RECENTLY, dummyCurrentTime));

        // testing "in a few days"
        expectedCalendarStart.set(2018, 10, 17, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 23, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_A_FEW_DAYS, dummyCurrentTime));
    }

    @Test
    public void parseDate_validInput_wrapAroundMonth() throws ParseException {
        // testing "in 3 weeks"
        expectedCalendarStart.set(2018, 11, 3, 9, 0, 0);
        expectedCalendarEnd.set(2018, 11, 9, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_SMALL_WEEKS, dummyCurrentTime));

        // testing "in 3 weeks" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 11, 3, 9, 0, 0);
        expectedCalendarEnd.set(2018, 11, 9, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_SMALL_WEEKS, dummySundayTime));

        // testing "next month"
        expectedCalendarStart.set(2018, 11, 1, 9, 0, 0);
        expectedCalendarEnd.set(2018, 11, 31, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_NEXT_MONTH, dummyCurrentTime));
    }

    @Test
    public void parseDate_validInput_wrapAroundYear() throws ParseException {
        // testing "in 365 days"
        expectedCalendarStart.set(2019, 10, 16, 9, 0, 0);
        expectedCalendarEnd.set(2019, 10, 16, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_BIG_DAYS, dummyCurrentTime));

        // testing "in 50 weeks"
        expectedCalendarStart.set(2019, 9, 28, 9, 0, 0);
        expectedCalendarEnd.set(2019, 10, 3, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_BIG_WEEKS, dummyCurrentTime));

        // testing "in 50 weeks" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2019, 9, 28, 9, 0, 0);
        expectedCalendarEnd.set(2019, 10, 3, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_BIG_WEEKS, dummySundayTime));

        // testing "in 5 months"
        expectedCalendarStart.set(2019, 3, 1, 9, 0, 0);
        expectedCalendarEnd.set(2019, 3, 30, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_SMALL_MONTHS, dummyCurrentTime));

        // testing "in 99 months"
        expectedCalendarStart.set(2027, 1, 1, 9, 0, 0);
        expectedCalendarEnd.set(2027, 1, 28, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_BIG_MONTHS, dummyCurrentTime));
    }

    @Test
    public void parseTimeSlot_validInput() {

    }

    @Test
    public void parseTimeSlot_invalidInput_throwsParseException() {

    }

    @Test
    public void parseDate_invalidInput_throwsParseException() {

    }

    private void zeroOutMilliseconds(Calendar... calendars) {
        for (Calendar calendar: calendars) {
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }

    private void setFirstDayOfWeekToMonday (Calendar... calendars) {
        for (Calendar calendar: calendars) {
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
        }
    }

    private void setFirstDayOfWeekToDefault(Calendar... calendars) {
        for (Calendar calendar: calendars) {
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        }
    }
}
