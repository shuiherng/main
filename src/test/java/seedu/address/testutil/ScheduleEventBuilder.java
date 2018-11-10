package seedu.address.testutil;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.Pair;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.person.PersonId;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building ScheduleEvent objects.
 */
public class ScheduleEventBuilder {

    public static final String SAMPLE_EVENTID = "e1";
    public static final Pair<Calendar> DEFAULT_VALID_DURATION;
    public static final String DEFAULT_PERSON_ID = "p1";
    public static final String DEFAULT_DATETIME_STRING = "next week";
    public static final String DEFAULT_DETAILS = "third visit";

    private PersonId personId;
    private Pair<Calendar> dateTime;
    private String details;
    private Set<Tag> tags;

    static {
        Calendar currentTime = Calendar.getInstance();
        Calendar sampleStart = (Calendar) currentTime.clone();
        Calendar sampleEnd = (Calendar) currentTime.clone();
        sampleStart.set(2018, 11, 13, 9, 30, 0);
        sampleEnd.set(2018, 11, 13, 10, 30, 0);
        DEFAULT_VALID_DURATION = new Pair<>(sampleStart, sampleEnd);
    }

    public ScheduleEventBuilder() {
        personId = new PersonId(DEFAULT_PERSON_ID, false);
        dateTime = DEFAULT_VALID_DURATION;
        details = DEFAULT_DETAILS;
        tags = new HashSet<>();
    }

    /**
     * Initializes the ScheduleEventBuilder with the data of {@code scheduleEventToCopy}.
     */
    public ScheduleEventBuilder(ScheduleEvent scheduleEventToCopy) {
        personId = scheduleEventToCopy.getPersonId();
        dateTime = scheduleEventToCopy.getDate();
        details = scheduleEventToCopy.getDetails();
        tags = scheduleEventToCopy.getTags();
    }

    /**
     * Sets the {@code PersonId} of the {@code ScheduleEvent} that we are building.
     */
    public ScheduleEventBuilder withPersonId(String personId) {
        this.personId = new PersonId(personId, false);
        return this;
    }

    /**
     * Sets the {@code details} of the {@code ScheduleEvent} that we are building.
     */
    public ScheduleEventBuilder withDetails(String details) {
        this.details = details;
        return this;
    }

    /**
     * Sets the {@code dateTime} of the {@code ScheduleEvent} that we are building.
     */
    public ScheduleEventBuilder withDurations(Pair<Calendar> duration) {
        this.dateTime = duration;
        return this;
    }
    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code ScheduleEvent} that we are building.
     */
    public ScheduleEventBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public ScheduleEvent build() {
        return new ScheduleEvent(dateTime, personId, details, tags);
    }

}
