package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import seedu.address.commons.util.Pair;
import seedu.address.model.Schedule;
import seedu.address.model.event.ScheduleEvent;

/**
 * Typical Events
 */
public class TypicalEvents {

    /* TODO: copy the event properties exactly out of
    XmlSerializableScheduleTest/typicalEventsSchedule.xml*/
    public static final ScheduleEvent e1;
    public static final ScheduleEvent e2;
    public static final ScheduleEvent e3;
    public static final ScheduleEvent e4;
    public static final ScheduleEvent e5;

    static {
        Calendar e1Start = Calendar.getInstance();
        e1Start.set(2018, Calendar.OCTOBER, 30, 9, 0, 0);
        Calendar e1End = Calendar.getInstance();
        e1End.set(2018, Calendar.OCTOBER, 30, 10, 0, 0);
        e1 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e1Start, e1End))
                .withPersonId("p58")
                .withDetails("some details")
                .build();

        Calendar e2Start = Calendar.getInstance();
        e2Start.set(2018, Calendar.NOVEMBER, 1, 14, 27, 0);
        Calendar e2End = Calendar.getInstance();
        e2End.set(2018, Calendar.NOVEMBER, 1, 15, 53, 0);
        e2 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e2Start, e2End))
                .withPersonId("p57")
                .withDetails("other details")
                .withTags("some")
                .build();

        Calendar e3Start = Calendar.getInstance();
        e3Start.set(2018, Calendar.NOVEMBER, 10, 9, 0, 0);
        Calendar e3End = Calendar.getInstance();
        e3End.set(2018, Calendar.NOVEMBER, 10, 10, 0, 0);
        e3 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e3Start, e3End))
                .withPersonId("p47")
                .withDetails("more details")
                .build();

        Calendar e4Start = Calendar.getInstance();
        e4Start.set(2018, Calendar.NOVEMBER, 10, 11, 0, 0);
        Calendar e4End = Calendar.getInstance();
        e4End.set(2018, Calendar.NOVEMBER, 10, 13, 0, 0);
        e4 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e4Start, e4End))
                .withPersonId("p51")
                .withDetails("test details")
                .build();

        Calendar e5Start = Calendar.getInstance();
        e5Start.set(2018, Calendar.NOVEMBER, 10, 14, 0, 0);
        Calendar e5End = Calendar.getInstance();
        e5End.set(2018, Calendar.NOVEMBER, 10, 15, 0, 0);
        e5 = new ScheduleEventBuilder()
                .withDurations(new Pair<>(e5Start, e5End))
                .withPersonId("p51")
                .withDetails("details")
                .build();
    }

    public static Schedule getTypicalSchedule() {
        Schedule s = new Schedule();
        for (ScheduleEvent scheduleEvent : getTypicalEvents()) {
            s.addScheduleEvent(scheduleEvent);
        }
        return s;
    }

    public static List<ScheduleEvent> getTypicalEvents() {
        return new ArrayList<>(Arrays.asList(e1, e2, e3, e4, e5));
    }
}
