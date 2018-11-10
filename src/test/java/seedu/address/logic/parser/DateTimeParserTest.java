
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.DateTimeParser.MESSAGE_END_BEFORE_START;
import static seedu.address.logic.parser.DateTimeParser.MESSAGE_HAVE_SLOTS;
import static seedu.address.logic.parser.DateTimeParser.MESSAGE_INVALID_INTEGER;
import static seedu.address.logic.parser.DateTimeParser.MESSAGE_INVALID_SLOT;
import static seedu.address.logic.parser.DateTimeParser.MESSAGE_NO_SLOTS;
import static seedu.address.logic.parser.Prompt.MESSAGE_PROMPT_TIMESLOT_FORMAT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.util.Pair;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.testutil.ScheduleEventBuilder;


public class DateTimeParserTest {

    private static final String EXPECTED_PARSE_EXCEPTION_NOT_THROWN = "The expected ParseException was not thrown.";
    private static final String VALID_TOMORROW = "tomorrow";
    private static final String VALID_THE_DAY_AFTER_TOMORROW = "the day after tomorrow";
    private static final String VALID_IN_SOME_SMALL_DAYS = "in 5 days";
    private static final String VALID_IN_SOME_BIG_DAYS = "in 365 days";
    private static final String VALID_IN_SOME_SMALL_WEEKS = "in 1 week";
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
    private static final String INVALID_SPECIFIED_DAY_NUMBER = "32/12/2018";
    private static final String INVALID_SPECIFIED_MONTH_NUMBER = "13/13/2018";
    private static final String INVALID_NON_INTEGER_NUMBER = "in 0.8 days";
    private static final String INVALID_ZERO_NUMBER = "in 0 week";
    private static final String INVALID_NEGATIVE_INTEGER_NUMBER = "in -1 day";
    private static final String INVALID_BIG_INTEGER_NUMBER = "in 1000000000000000000 months";
    private static final String INVALID_CAPITALIZED = "neXt Month";
    private static final String INVALID_RANDOM = "elephant";

    private static final String REFINED_VALID_TIME_SLOT_EARLY = "13/12/2018 9:00 - 10:30";
    private static final String REFINED_VALID_TIME_SLOT_MIDDLE = "13/12/2018 12:00 - 14:00";
    private static final String REFINED_VALID_TIME_SLOT_LATE = "13/12/2018 16:00 - 18:00";
    private static final String REFINED_VALID_TIME_SLOT_WITH_WHITESPACE = "13/12/2018 9:30 -   10:30";
    private static final String REFINED_INVALID_INCOMPLETE = "13/12 11:00 -";
    private static final String REFINED_INVALID_WRONG_DATE = "32/12/2018 9:30 - 10:30";
    private static final String REFINED_INVALID_WRONG_TIME = "13/12/2018 9:60 - 10:30";
    private static final String REFINED_INVALID_TOO_EARLY = "13/12/2018 8:30 - 10:00";
    private static final String REFINED_INVALID_T00_LATE = "13/12/2018 18:00 - 23:30";
    private static final String REFINED_INVALID_END_EARLIER_THAN_START = "13/12/2018 10:30 - 9:30";
    private static final String REFINED_INVALID_FORMAT_TIME_SLOT = "13.12.2018 9:30 - 10:30";
    private static final String REFINED_INVALID_FORMAT_WRONG_SEQUENCE_TIME_SLOT = "9:30 - 10:30 13/12/2018";


    private DateTimeParser parser;
    private Calendar actualCurrentTime;
    private Calendar dummyCurrentTime;
    private Calendar dummySundayTime;
    private Calendar expectedCalendarStart;
    private Calendar expectedCalendarEnd;
    private List<ScheduleEvent> scheduledAppointments;
    private Calendar durationStart;
    private Calendar durationEnd;
    private Pair<Calendar> duration;
    private Calendar scheduleEventStart;
    private Calendar scheduleEventEnd;

    @Before
    public void initializeCalendars() {
        parser = new DateTimeParser();
        actualCurrentTime = Calendar.getInstance();
        dummyCurrentTime = (Calendar) actualCurrentTime.clone();
        dummySundayTime = (Calendar) actualCurrentTime.clone();
        dummyCurrentTime.set(2018, 10, 16, 16, 13, 20);
        dummySundayTime.set(2018, 10, 18, 16, 13, 20);
        expectedCalendarStart = (Calendar) actualCurrentTime.clone();
        expectedCalendarEnd = (Calendar) actualCurrentTime.clone();
        zeroOutMilliseconds(expectedCalendarStart, expectedCalendarEnd);
        scheduledAppointments = new ArrayList<>();
        durationStart = (Calendar) actualCurrentTime.clone();
        durationEnd = (Calendar) actualCurrentTime.clone();
        duration = new Pair<>(durationStart, durationEnd);
        scheduleEventStart = (Calendar) actualCurrentTime.clone();
        scheduleEventEnd = (Calendar) actualCurrentTime.clone();
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
        // testing "in 1 week"
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
        setFirstDayOfWeekToMonday(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseDate(VALID_IN_SOME_SMALL_WEEKS, dummyCurrentTime));

        // testing "in 1 week" with a Sunday time
        // as Sundays are handled a bit differently
        expectedCalendarStart.set(2018, 10, 19, 9, 0, 0);
        expectedCalendarEnd.set(2018, 10, 25, 18, 0, 0);
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
    public void parseTimeSlot_validInput() throws ParseException {
        // testing "13/12/2018 9:00 - 10:30"
        expectedCalendarStart.set(2018, 11, 13, 9, 0, 0);
        expectedCalendarEnd.set(2018, 11, 13, 10, 30, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseTimeSlot(REFINED_VALID_TIME_SLOT_EARLY));

        // testing "13/12/2018 12:00 - 14:00"
        expectedCalendarStart.set(2018, 11, 13, 12, 0, 0);
        expectedCalendarEnd.set(2018, 11, 13, 14, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseTimeSlot(REFINED_VALID_TIME_SLOT_MIDDLE));

        // testing "13/12/2018 16:00 - 18:00"
        expectedCalendarStart.set(2018, 11, 13, 16, 0, 0);
        expectedCalendarEnd.set(2018, 11, 13, 18, 0, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseTimeSlot(REFINED_VALID_TIME_SLOT_LATE));

        // testing "13/12/2018 9:30 -   10:30"
        expectedCalendarStart.set(2018, 11, 13, 9, 30, 0);
        expectedCalendarEnd.set(2018, 11, 13, 10, 30, 0);
        setFirstDayOfWeekToDefault(expectedCalendarStart, expectedCalendarEnd);
        assertEquals(new Pair<>(expectedCalendarStart, expectedCalendarEnd),
                parser.parseTimeSlot(REFINED_VALID_TIME_SLOT_WITH_WHITESPACE));
    }

    @Test
    public void parseTimeSlot_invalidInput_throwsParseException() {
        // testing "13.12.2018 9:30 - 10:30"
        assertTimeSlotParsingFailure(REFINED_INVALID_FORMAT_TIME_SLOT,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "9:30 - 10:30 13/12/2018"
        assertTimeSlotParsingFailure(REFINED_INVALID_FORMAT_WRONG_SEQUENCE_TIME_SLOT,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "13/12/2018 10:30 - 9:30"
        assertTimeSlotParsingFailure(REFINED_INVALID_END_EARLIER_THAN_START,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_END_BEFORE_START));

        // testing "13/12/2018 18:00 - 23:30"
        assertTimeSlotParsingFailure(REFINED_INVALID_T00_LATE,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "13/12/2018 8:30 - 10:00"
        assertTimeSlotParsingFailure(REFINED_INVALID_TOO_EARLY,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "32/12/2018 9:30 - 10:30"
        assertTimeSlotParsingFailure(REFINED_INVALID_WRONG_DATE,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "13/12/2018 9:60 - 10:30"
        assertTimeSlotParsingFailure(REFINED_INVALID_WRONG_TIME,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));

        // testing "13/12 11:00 -"
        assertTimeSlotParsingFailure(REFINED_INVALID_INCOMPLETE,
                String.format(MESSAGE_INVALID_SLOT, MESSAGE_PROMPT_TIMESLOT_FORMAT));
    }

    @Test
    public void parseDate_invalidInput_throwsParseException() {
        // testing "elephant"
        assertDateParsingFailure(INVALID_RANDOM, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "13.12.2018"
        assertDateParsingFailure(INVALID_SPECIFIED_FORMAT, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "32/12/2018"
        assertDateParsingFailure(INVALID_SPECIFIED_DAY_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "13/13/2018"
        assertDateParsingFailure(INVALID_SPECIFIED_MONTH_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "neXt Month"
        assertDateParsingFailure(INVALID_CAPITALIZED, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "in -1 day"
        assertDateParsingFailure(INVALID_NEGATIVE_INTEGER_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE_APPOINTMENT));

        // testing "in 1000000000000000000 months"
        assertDateParsingFailure(INVALID_BIG_INTEGER_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INTEGER));

        // testing "in 0.8 days"
        assertDateParsingFailure(INVALID_NON_INTEGER_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INTEGER));

        // testing "in 0 week"
        assertDateParsingFailure(INVALID_ZERO_NUMBER, dummyCurrentTime,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INVALID_INTEGER));

    }

    @Test
    public void getAvailableTimeSlotsBetween_noAvailableTimeSlots() {
        // testing a single day
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 13, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(duration).build());
        assertEquals(MESSAGE_NO_SLOTS, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();

        // testing 3 days
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        scheduleEventStart.set(2018, 11, 13, 9, 00, 0);
        scheduleEventEnd.set(2018, 11, 13, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 14, 9, 00, 0);
        scheduleEventEnd.set(2018, 11, 14, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 15, 9, 00, 0);
        scheduleEventEnd.set(2018, 11, 15, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        assertEquals(MESSAGE_NO_SLOTS, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
    }

    @Test
    public void getAvailableTimeSlotsBetween_noEmptyDays() {
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        scheduleEventStart.set(2018, 11, 13, 10, 00, 0);
        scheduleEventEnd.set(2018, 11, 13, 13, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 14, 9, 00, 0);
        scheduleEventEnd.set(2018, 11, 14, 14, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 15, 15, 00, 0);
        scheduleEventEnd.set(2018, 11, 15, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        String expected = MESSAGE_HAVE_SLOTS
                        + "\n"
                        + "13/12/2018:\n"
                        + "09:00 - 10:00\n"
                        + "13:00 - 18:00\n"
                        + "\n"
                        + "14/12/2018:\n"
                        + "14:00 - 18:00\n"
                        + "\n"
                        + "15/12/2018:\n"
                        + "09:00 - 15:00\n";
        assertEquals(expected, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
    }

    @Test
    public void getAvailableTimeSlotsBetween_startWithEmptyDays() {
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        scheduleEventStart.set(2018, 11, 14, 12, 00, 0);
        scheduleEventEnd.set(2018, 11, 14, 14, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 15, 15, 00, 0);
        scheduleEventEnd.set(2018, 11, 15, 17, 30, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        String expected = MESSAGE_HAVE_SLOTS
                + "\n"
                + "13/12/2018:\n"
                + "09:00 - 18:00\n"
                + "\n"
                + "14/12/2018:\n"
                + "09:00 - 12:00\n"
                + "14:00 - 18:00\n"
                + "\n"
                + "15/12/2018:\n"
                + "09:00 - 15:00\n"
                + "17:30 - 18:00\n";
        assertEquals(expected, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
    }

    @Test
    public void getAvailableTimeSlotsBetween_endWithEmptyDays() {
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        scheduleEventStart.set(2018, 11, 14, 9, 01, 0);
        scheduleEventEnd.set(2018, 11, 14, 9, 02, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 13, 17, 59, 0);
        scheduleEventEnd.set(2018, 11, 13, 18, 00, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        String expected = MESSAGE_HAVE_SLOTS
                + "\n"
                + "13/12/2018:\n"
                + "09:00 - 17:59\n"
                + "\n"
                + "14/12/2018:\n"
                + "09:00 - 09:01\n"
                + "09:02 - 18:00\n"
                + "\n"
                + "15/12/2018:\n"
                + "09:00 - 18:00\n";
        assertEquals(expected, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
    }

    @Test
    public void getAvailableTimeSlotsBetween_emptyDaysInBetween() {
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        scheduleEventStart.set(2018, 11, 15, 12, 27, 0);
        scheduleEventEnd.set(2018, 11, 15, 14, 56, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 13, 9, 59, 0);
        scheduleEventEnd.set(2018, 11, 13, 10, 30, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        scheduleEventStart.set(2018, 11, 13, 12, 59, 0);
        scheduleEventEnd.set(2018, 11, 13, 13, 30, 0);
        scheduledAppointments.add(new ScheduleEventBuilder().withDurations(
                new Pair<>((Calendar) scheduleEventStart.clone(), (Calendar) scheduleEventEnd.clone())).build());
        String expected = MESSAGE_HAVE_SLOTS
                + "\n"
                + "13/12/2018:\n"
                + "09:00 - 09:59\n"
                + "10:30 - 12:59\n"
                + "13:30 - 18:00\n"
                + "\n"
                + "14/12/2018:\n"
                + "09:00 - 18:00\n"
                + "\n"
                + "15/12/2018:\n"
                + "09:00 - 12:27\n"
                + "14:56 - 18:00\n";
        assertEquals(expected, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
    }

    @Test
    public void getAvailableTimeSlotsBetween_allEmptyDays() {
        durationStart.set(2018, 11, 13, 9, 00, 0);
        durationEnd.set(2018, 11, 15, 18, 00, 0);
        String expected = MESSAGE_HAVE_SLOTS
                + "\n"
                + "13/12/2018:\n"
                + "09:00 - 18:00\n"
                + "\n"
                + "14/12/2018:\n"
                + "09:00 - 18:00\n"
                + "\n"
                + "15/12/2018:\n"
                + "09:00 - 18:00\n";
        assertEquals(expected, parser.getAvailableTimeSlotsBetween(scheduledAppointments, duration));
        scheduledAppointments.clear();
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
        for (Calendar calendar : calendars) {
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        }
    }

    /**
     * Utility method for asserting that an exception has been thrown for date parsing.
     * @param input The user input.
     * @param currentTime The current time.
     * @param expectedMessage The expected exception message.
     */
    private void assertDateParsingFailure(String input, Calendar currentTime, String expectedMessage) {
        try {
            parser.parseDate(input, currentTime);
            throw new AssertionError(EXPECTED_PARSE_EXCEPTION_NOT_THROWN);
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }

    /**
     * Utility method for asserting that an exception has been thrown for time slot parsing.
     * @param input The user input.
     * @param expectedMessage The expected exception message.
     */
    private void assertTimeSlotParsingFailure(String input, String expectedMessage) {
        try {
            parser.parseTimeSlot(input);
            throw new AssertionError(EXPECTED_PARSE_EXCEPTION_NOT_THROWN);
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }


}
