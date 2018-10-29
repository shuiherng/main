package seedu.address.storage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
// import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlySchedule;
import seedu.address.model.Schedule;
import seedu.address.model.event.ScheduleEvent;

/**
 * An Immutable Schedule that is serializable to XML format
 */
@XmlRootElement(name = "schedule")
public class XmlSerializableSchedule {

    public static final String MESSAGE_DUPLICATE_EVENT = "Event list contains duplicate event(s).";

    @XmlElement
    private List<XmlAdaptedEvent> events;

    /**
     * Creates an empty XmlSerializableSchedule.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableSchedule() { events = new ArrayList<>(); }

    /**
     * Conversion
     */
    public XmlSerializableSchedule(ReadOnlySchedule src) {
        this();
        events.addAll(src.getAllEventList().stream().map(XmlAdaptedEvent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this schedule into the scheduleModels' {@code Schedule} object.
     *
     * @throws IllegalValueException if there was any data constraints violated or duplicates in the
     * {@code XmlAdaptedEvent}.
     */
    public Schedule toModelType() throws IllegalValueException, ParseException {
        Schedule schedule = new Schedule();
        for (XmlAdaptedEvent e : events) {
            ScheduleEvent event = e.toModelType();
            if (schedule.hasScheduleEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }
            schedule.addScheduleEvent(event);
        }
        return schedule;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableSchedule)) {
            return false;
        }
        return events.equals(((XmlSerializableSchedule) other).events);
    }
}
