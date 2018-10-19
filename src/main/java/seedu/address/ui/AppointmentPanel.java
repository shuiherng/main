package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AppointmentPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
// import seedu.address.model.Schedule;
import seedu.address.model.event.ScheduleEvent;

/**
 * Panel containing the list of schedule events.
 */
public class AppointmentPanel extends UiPart<Region> {
    private static final String FXML = "AppointmentPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AppointmentPanel.class);

    @FXML
    private ListView<ScheduleEvent> appointmentView;

    public AppointmentPanel(ObservableList<ScheduleEvent> scheduleEventList) {
        super(FXML);
        setConnections(scheduleEventList);
        registerAsAnEventHandler(this);
    }

    /**
     * Sets connections
     * @param scheduleEventList schedule event list
     */
    private void setConnections(ObservableList<ScheduleEvent> scheduleEventList) {
        appointmentView.setItems(scheduleEventList);
        appointmentView.setCellFactory(listView -> new AppointmentViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    /**
     * Event handler
     */
    private void setEventHandlerForSelectionChangeEvent() {
        appointmentView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in appointment panel changed to : '" + newValue + "'");
                        raise(new AppointmentPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code AppointmentCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            appointmentView.scrollTo(index);
            appointmentView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ScheduleEvent} using a {@code AppointmentCard}.
     */
    class AppointmentViewCell extends ListCell<ScheduleEvent> {
        @Override
        protected void updateItem(ScheduleEvent event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new AppointmentCard(event, getIndex() + 1).getRoot());
            }
        }
    }

}
