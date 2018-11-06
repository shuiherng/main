package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;

/**
 * The person browse panel of the App
 */
public class PersonBrowsePanel extends UiPart<Region> {

    private static final String DEFAULT_MESSAGE = "You have not selected any patient";

    private static final String FXML = "PersonBrowsePanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private Label phone;

    @FXML
    private Label address;

    @FXML
    private Label email;

    public PersonBrowsePanel() {
        super(FXML);

        loadDefaultBrowse();
        registerAsAnEventHandler(this);
    }

    /**
     *  load the default browse panel
     */
    public void loadDefaultBrowse() {
        name.setText(DEFAULT_MESSAGE);
        id.setText(DEFAULT_MESSAGE);
        phone.setText(DEFAULT_MESSAGE);
        address.setText(DEFAULT_MESSAGE);
        email.setText(DEFAULT_MESSAGE);
    }

    /**
     * load the browse panel
     *
     * @param person
     */
    private void loadBrowse(Person person) {
        name.setText(person.getName().toString());
        id.setText(person.getId().toString());
        phone.setText(person.getPhone().toString());
        address.setText(person.getAddress().toString());
        email.setText(person.getEmail().toString());
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadBrowse(event.getNewSelection());
    }
}
