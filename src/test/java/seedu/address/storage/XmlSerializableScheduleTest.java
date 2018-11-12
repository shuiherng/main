package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.ScheduleEventUtil.matchEventProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.Schedule;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.testutil.TypicalEvents;


public class XmlSerializableScheduleTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "XmlSerializableScheduleTest");
    private static final Path TYPICAL_EVENTS_FILE = TEST_DATA_FOLDER.resolve("typicalEventsSchedule.xml");
    private static final Path INVALID_EVENTS_FILE = TEST_DATA_FOLDER.resolve("invalidEventSchedule.xml");
    private static final Path DUPLICATE_EVENTS_FILE = TEST_DATA_FOLDER.resolve("duplicateEventSchedule.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void toModelType_typicalEventsFile_success() throws Exception {
        XmlSerializableSchedule dataFromFile = XmlUtil.getDataFromFile(TYPICAL_EVENTS_FILE,
                XmlSerializableSchedule.class);
        Schedule scheduleFromFile = dataFromFile.toModelType();
        Schedule typicalEventsSchedule = TypicalEvents.getTypicalSchedule();

        ObservableList<ScheduleEvent> eventList = scheduleFromFile.getAllEventList();
        ObservableList<ScheduleEvent> sampleList = typicalEventsSchedule.getAllEventList();

        assertEquals(eventList.size(), sampleList.size());

        for (ScheduleEvent event: eventList) {
            assertTrue(sampleList.filtered(e -> matchEventProperties(e, event)).size() == 1);
        }

    }

    @Test
    public void toModelType_invalidEventsFile_throwsIllegalValueException() throws Exception {
        XmlSerializableSchedule dataFromFile = XmlUtil.getDataFromFile(INVALID_EVENTS_FILE,
                XmlSerializableSchedule.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateEvents_throwsIllegalValueException() throws Exception {
        XmlSerializableSchedule dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_EVENTS_FILE,
                XmlSerializableSchedule.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableSchedule.MESSAGE_DUPLICATE_EVENT);
        dataFromFile.toModelType();
    }
}
