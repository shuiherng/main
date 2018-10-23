package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import seedu.address.model.ScheduleModelManager;

public class DateTimeParserTest {

    private DateTimeParser parser = new DateTimeParser(new ScheduleModelManager());

    private static final String VALID_TOMORROW = "tomorrow";
    private static final String VALID_THE_DAY_AFTER_TOMORROW = "the day after tomorrow";
    private static final String VALID_IN_SOME_SMALL_DAYS= "in 5 days";
    private static final String VALID_IN_SOME_BIG_DAYS= "in 365 days";
    private static final String VALID_IN_SOME_SMALL_WEEKS= "in 3 weeks";
    private static final String VALID_IN_SOME_BIG_WEEKS= "in 100 weeks";
    private static final String VALID_IN_SOME_SMALL_MONTHS= "in 2 months";
    private static final String VALID_IN_SOME_BIG_MONTHS= "in 99 months";
    private static final String VALID_THIS_WEEKDAY = "this Friday";
    private static final String VALID_NEXT_WEEKDAY = "next Monday";
    private static final String VALID_THIS_WEEK = "this week";
    private static final String VALID_NEXT_WEEK = "next week";
    private static final String VALID_THIS_MONTH = "this month";
    private static final String VALID_NEXT_MONTH = "next month";
    private static final String VALID_SOON = "soon";
    private static final String VALID_RECENTLY = "recently";
    private static final String VALID_IN_A_FEW_DAYS = "in a few days";
    private static final String VALID_SPECIFIED = "13/12/2018";

    private static final String INVALID_FORMAT_SPECIFIED = "13.12.2018";
    private static final String INVALID_DAY_NUMBER_SPECIFIED = "33/12/2018";
    private static final String INVALID_MONTH_NUMBER_SPECIFIED = "13/13/2018";
    private static final String INVALID_NON_INTEGER_NUMBER= "in 0.8 days";
    private static final String INVALID_CAPITALIZED = "neXt Month";
    private static final String INVALID_RANDOM= "elephant";

    private static final String REFINED_VALID_TIME_SLOT = "13/12/2018 9:30 - 10:30";
    private static final String REFINED_INVALID_TOO_EARLY = "13/12/2018 8:30 - 10:30";
    private static final String REFINED_INVALID_T00_LATE = "13/12/2018 19:30 - 23:30";
    private static final String REFINED_INVALID_END_EARLIER_THAN_START = "13/12/2018 10:30 - 9:30";
    private static final String REFINED_INVALID_FORMAT_TIME_SLOT = "13.12.2018 10:30 - 9:30";
    private static final String REFINED_INVALID_FORMAT_WRONG_SEQUENCE_TIME_SLOT = "10:30 - 9:30 13.12.2018";

    private Calendar actualCurrentTime;
    private Calendar convenientTime;
    private Calendar wrapAroundWeekTime;
    private Calendar wrapAroundMonthTime;
    private Calendar wrapAroundYearTime;

    @Before
    public void createCurrentTimeCalendars() {
        actualCurrentTime = Calendar.getInstance();
        convenientTime = (Calendar) actualCurrentTime.clone();
        wrapAroundWeekTime = (Calendar) actualCurrentTime.clone();
        wrapAroundMonthTime = (Calendar) actualCurrentTime.clone();
        wrapAroundYearTime = (Calendar) actualCurrentTime.clone();
        convenientTime.set(2018, 10, 8, 12, 13, 20); // no possible wrap around
        wrapAroundWeekTime.set(2018, 10, 14, 12, 13, 20); // need to wrap around week
        wrapAroundMonthTime.set(2018, 10, 31, 12, 13, 20); // need to wrap around month
        wrapAroundYearTime.set(2018, 12, 30, 12, 13, 20); // need to wrap around year
    }

    @Test
    public void parseDate_validInput_atConvenientTime() {
        // testing "tomorrow"
        Calendar expectedCalendarStart = (Calendar) actualCurrentTime.clone();
        Calendar expectedCalendarEnd = (Calendar) actualCurrentTime.clone();
        expectedCalendarStart.set(2018, 10, 9, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 9, 18, 0, 0);
        expectedCalendarStart.set(Calendar.MILLISECOND, 0);
        expectedCalendarEnd.set(Calendar.MILLISECOND, 0);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),  )


    }

    @Test
    public void parseDate_validInput_atWrapAroundWeekTime() {

    }

    @Test
    public void parseDate_validInput_atWrapAroundMonthTime() {

    }

    @Test
    public void parseDate_validInput_atWrapAroundYearTime() {

    }


    @Test
    public void parseRefinedTime_validInput() {

    }

    @Test
    public void parseRefinedTime_invalidInput_throwsParseException() {

    }
    @Test
    public void parseDate_invalidInput_throwsParseException() {

    }
}