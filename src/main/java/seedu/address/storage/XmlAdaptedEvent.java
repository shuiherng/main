package seedu.address.storage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Calendar;
import javafx.util.Pair;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.EventID;
import seedu.address.model.person.PersonID;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Event.
 */
public class XmlAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    @XmlElement(required = true)
    private String eventID;

    @XmlElement(required = true)
    private String datetimeStart;

    @XmlElement(required = true)
    private String datetimeEnd;

    @XmlElement(required = true)
    private String personID;

    @XmlElement(required = true)
    private String details;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {}

    /**
     * Construcst an {@code XmlAdaptedEvent} with the given event details.
     */
    public XmlAdaptedEvent(String eventID, String datetimeStart, String datetimeEnd, String personID, String details, List<XmlAdaptedTag> tagged) {
        this.eventID = eventID;
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
        this.personID = personID;
        this.details = details;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Event into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEvent
     */
    public XmlAdaptedEvent(ScheduleEvent source) {
        eventID = source.getID().toString();
        datetimeStart = ScheduleEvent.sdf.format(source.getDate().getKey().getTime());
        datetimeEnd = ScheduleEvent.sdf.format(source.getDate().getValue().getTime());
        personID = source.getPersonID().toString();
        details = source.getDetails();
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    public ScheduleEvent toModelType() throws IllegalValueException, ParseException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        if (eventID == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, EventID.class.getSimpleName()));
        }
        if (!EventID.isValidID(eventID)) {
            throw new IllegalValueException(EventID.MESSAGE_EVENTID_CONSTRAINTS);
        }
        final EventID modelEventID = new EventID(eventID);

        if (datetimeStart == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Calendar Start"));
        }
        if (ScheduleEvent.sdf.parse(datetimeStart) == null) {
            throw new IllegalValueException("Unable to parse datetime start");
        }
        if (datetimeEnd == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Calendar End"));
        }
        if (ScheduleEvent.sdf.parse(datetimeEnd) == null) {
            throw new IllegalValueException("Unable to parse datetime end");
        }
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(ScheduleEvent.sdf.parse(datetimeStart));
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(ScheduleEvent.sdf.parse(datetimeEnd));
        final Pair<Calendar, Calendar> modelCalendarPair = new Pair<>(startTime, endTime);

        if (personID == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PersonID.class.getSimpleName()));
        }
        if (!PersonID.isValidID(personID)) {
            throw new IllegalValueException(PersonID.MESSAGE_PERSONID_CONSTRAINTS);
        }
        final PersonID modelPersonID = new PersonID(personID);

        if (details == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Details"));
        }
        final String modelDetails = details;

        final Set<Tag> modelTags = new HashSet<>(eventTags);
        return new ScheduleEvent(modelEventID, modelCalendarPair, modelPersonID, modelDetails, modelTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEvent)) {
            return false;
        }

        XmlAdaptedEvent otherEvent = (XmlAdaptedEvent) other;
        return Objects.equals(eventID, otherEvent.eventID)
                && Objects.equals(datetimeStart, otherEvent.datetimeStart)
                && Objects.equals(datetimeEnd, otherEvent.datetimeEnd)
                && Objects.equals(personID, otherEvent.personID)
                && Objects.equals(details, otherEvent.details)
                && tagged.equals(otherEvent.tagged);
    }
}
