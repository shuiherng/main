package seedu.address.logic.parser;

import seedu.address.MainApp;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.PromptException;
import seedu.address.ui.PromptWindow;

import java.util.Scanner;

/**
 * Prompt handler for users.
 */
public class Prompt {

    public static final String LEADING_MESSAGE = "Please enter a time slot : ";
    private MainApp mainApp = new MainApp();
    private PromptWindow promptWindow;

    /**
     * Gets more input from the user
     * @param messageToUser message to be displayed
     * @return input by the user
     */
    public String promptForMoreInput (String messageToUser) throws PromptException {
        promptWindow = mainApp.showPromptWindow(LEADING_MESSAGE + messageToUser);
        if (promptWindow.isEnterClicked()) {
            return promptWindow.getInput();
        } else {
            throw new PromptException();
        }
    }
}
