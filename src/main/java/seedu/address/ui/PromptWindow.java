package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Prompt Window to request for further information
 */
public class PromptWindow {

    @FXML
    private TextField commandBox = new TextField();

    @FXML
    private TextArea displayBox = new TextArea();

    private Stage promptStage;
    private String input;
    private boolean enterClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this prompt window.
     *
     * @param promptStage the stage
     */
    public void setPromptStage(Stage promptStage) {
        this.promptStage = promptStage;
    }

    /**
     * Sets the message to be displayed in the box.
     *
     * @param message the message sent to the user
     */
    public void setDisplay(String message) {
        displayBox.setText(message);
    }

    /*
     * Returns the input from the user
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns true if the user clicked Enter, false otherwise.
     *
     * @return
     */
    public boolean isEnterClicked() {
        return enterClicked;
    }

    /**
     * Called when the user clicks enter.
     */
    @FXML
    private void handleEnter() {
        input = commandBox.getText();
        enterClicked = true;
        promptStage.close();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        promptStage.close();
        enterClicked = false;
    }

}