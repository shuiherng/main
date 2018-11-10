package seedu.address.storage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.Pair;
import seedu.address.model.event.EventId;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.PersonId;
import seedu.address.model.tag.Tag;


/**
 * JAXB-friendly version of the Event.
 */
public class XmlAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    @XmlElement(required = true)
    private String eventId;

    @XmlElement(required = true)
    private String datetimeStart;

    @XmlElement(required = true)
    private String datetimeEnd;

    @XmlElement(required = true)
    private String personId;

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
     * Constructs an {@code XmlAdaptedEvent} with the given event details.
     */
    public XmlAdaptedEvent(String eventId, String datetimeStart, String datetimeEnd, String personId,
                           String details, List<XmlAdaptedTag> tagged) {
        this.eventId = eventId;
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
        this.personId = personId;
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
        eventId = source.getId().toString();
        datetimeStart = ScheduleEvent.STORAGE_SDF.format(source.getDate().getKey().getTime());
        datetimeEnd = ScheduleEvent.STORAGE_SDF.format(source.getDate().getValue().getTime());
        personId = source.getPersonId().toString();
        details = source.getDetails();
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
    }

    /**
     * Exports the data to an object acceptable by the application.
     * @return ScheduleEvent type object
     * @throws IllegalValueException illegal value
     * @throws ParseException parse exception
     */
    public ScheduleEvent toModelType() throws IllegalValueException, ParseException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        if (eventId == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, EventId.class.getSimpleName()));
        }
        if (!EventId.isValidId(eventId)) {
            throw new IllegalValueException(EventId.MESSAGE_EVENTID_CONSTRAINTS);
        }
        final EventId modelEventId = new EventId(eventId, true);

        if (datetimeStart == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Calendar Start"));
        }
        if (ScheduleEvent.STORAGE_SDF.parse(datetimeStart) == null) {
            throw new IllegalValueException("Unable to parse datetime start");
        }
        if (datetimeEnd == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Calendar End"));
        }
        if (ScheduleEvent.STORAGE_SDF.parse(datetimeEnd) == null) {
            throw new IllegalValueException("Unable to parse datetime end");
        }

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(ScheduleEvent.STORAGE_SDF.parse(datetimeStart));
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(ScheduleEvent.STORAGE_SDF.parse(datetimeEnd));
        final Pair<Calendar> modelCalendarPair = new Pair<>(startTime, endTime);

        if (personId == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, PersonId.class.getSimpleName()));
        }
        if (!PersonId.isValidId(personId)) {
            throw new IllegalValueException(PersonId.MESSAGE_PERSONID_CONSTRAINTS);
        }
        final PersonId modelPersonId = new PersonId(personId, false);

        if (details == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Details"));
        }
        final String modelDetails = details;

        final Set<Tag> modelTags = new HashSet<>(eventTags);
        return new ScheduleEvent(modelEventId, modelCalendarPair, modelPersonId, modelDetails, modelTags);
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
        return Objects.equals(eventId, otherEvent.eventId)
                && Objects.equals(datetimeStart, otherEvent.datetimeStart)
                && Objects.equals(datetimeEnd, otherEvent.datetimeEnd)
                && Objects.equals(personId, otherEvent.personId)
                && Objects.equals(details, otherEvent.details)
                && tagged.equals(otherEvent.tagged);
    }
}
