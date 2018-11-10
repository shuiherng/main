package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.event.ScheduleEvent;

/**
 * Provides a handle to an appointment card in the appointment panel.
 */
public class AppointmentCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String DATETIME_ID = "#datetime";
    private static final String PERSONID_ID = "#personId";
    private static final String DETAILS_ID = "#details";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label datetimeLabel;
    private final Label personidLabel;
    private final Label detailsLabel;
    private final List<Label> tagLabels;

    public AppointmentCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        datetimeLabel = getChildNode(DATETIME_ID);
        personidLabel = getChildNode(PERSONID_ID);
        detailsLabel = getChildNode(DETAILS_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getDatetime() {
        return datetimeLabel.getText();
    }

    public String getPersonid() {
        return personidLabel.getText();
    }

    public String getDetails() {
        return detailsLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code event}.
     */
    public boolean equals(ScheduleEvent event) {
        return getDatetime().equals(event.getDate().toString())
                && getPersonid().equals(event.getPersonId().value)
                && getDetails().equals(event.getDetails())
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(event.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
