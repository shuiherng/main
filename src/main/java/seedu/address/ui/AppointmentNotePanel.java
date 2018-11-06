package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AppointmentPanelSelectionChangedEvent;

/**
 * The appointment note panel of the App
 */
public class AppointmentNotePanel extends UiPart<Region> {

    private static final String DEFAULT_MESSAGE = "You have not selected any appointment";

    private static final String FXML = "AppointmentNotePanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private TextArea notearea;

    public AppointmentNotePanel() {
        super(FXML);

        loadDefaultNote();
        registerAsAnEventHandler(this);
    }

    public void loadDefaultNote() {
        notearea.setText(DEFAULT_MESSAGE);
    }

    private void loadNotes(String notes) {
        notearea.setText(notes);
    }

    @Subscribe
    private void handleAppointmentPanelSelectionChangedEvent(AppointmentPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadNotes(event.getNewSelection().getDetails());
    }
}
