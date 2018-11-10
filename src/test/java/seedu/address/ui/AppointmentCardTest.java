package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysEvent;

import java.util.Calendar;

import org.junit.Test;

import guitests.guihandles.AppointmentCardHandle;
import seedu.address.commons.util.Pair;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.testutil.ScheduleEventBuilder;

public class AppointmentCardTest extends GuiUnitTest {

    private Calendar currentTime = Calendar.getInstance();
    private Calendar sampleStart = (Calendar) currentTime.clone();
    private Calendar sampleEnd = (Calendar) currentTime.clone();

    @Test
    public void display() {
        // no tags
        ScheduleEvent eventWithNoTags = new ScheduleEventBuilder().withTags(new String[0]).build();
        AppointmentCard appointmentCard = new AppointmentCard(eventWithNoTags, 1);
        uiPartRule.setUiPart(appointmentCard);
        assertCardDisplay(appointmentCard, eventWithNoTags, 1);

        // with tags
        ScheduleEvent eventWithTags = new ScheduleEventBuilder().build();
        appointmentCard = new AppointmentCard(eventWithTags, 2);
        uiPartRule.setUiPart(appointmentCard);
        assertCardDisplay(appointmentCard, eventWithTags, 2);
    }

    @Test
    public void equals() {
        ScheduleEvent event = new ScheduleEventBuilder().build();
        AppointmentCard appointmentCard = new AppointmentCard(event, 0);

        // same event, same index -> returns true
        AppointmentCard copy = new AppointmentCard(event, 0);
        assertTrue(appointmentCard.equals(copy));

        // same object -> returns true
        assertTrue(appointmentCard.equals(appointmentCard));
;
        // null -> returns false
        assertFalse(appointmentCard.equals(null));

        // different types -> returns false
        assertFalse(appointmentCard.equals(0));

        // different event, same index -> returns false
        ScheduleEvent differentEvent = new ScheduleEventBuilder().withDurations(new Pair<>(sampleStart, sampleEnd))
                .build();
        assertFalse(appointmentCard.equals(new AppointmentCard(differentEvent, 0)));

        // same event, different index -> returns false
        assertFalse(appointmentCard.equals(new AppointmentCard(event, 1)));
    }

    /**
     * Asserts that {@code appointmentCard} displays the details of {@code expectedEvent} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(AppointmentCard appointmentCard, ScheduleEvent expectedEvent, int expectedId) {
        guiRobot.pauseForHuman();

        AppointmentCardHandle appointmentCardHandle = new AppointmentCardHandle(appointmentCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", appointmentCardHandle.getId());

        // verify event details are displayed correctly
        assertCardDisplaysEvent(expectedEvent, appointmentCardHandle);
    }
}
