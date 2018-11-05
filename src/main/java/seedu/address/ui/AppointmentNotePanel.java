package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AppointmentPanelSelectionChangedEvent;

import java.util.logging.Logger;

/*
 * The appointment note panel of the App
 */
public class AppointmentNotePanel extends UiPart<Region> {
    private static final String FXML = "AppointmentNotePanel.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    public AppointmentNotePanel(){
        super(FXML);

        registerAsAnEventHandler(this);
    }

    @FXML
    private TextArea notearea;

    private void loadNotes(String notes) {
        notearea.setText(notes);
    }

    @Subscribe
    private void handleAppointmentPanelSelectionChangedEvent(AppointmentPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadNotes(event.getNewSelection().getDetails());
    }
}
