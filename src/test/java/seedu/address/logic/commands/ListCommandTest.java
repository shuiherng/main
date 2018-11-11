package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_DISEASE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;

public class ListCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void listDisease_success() throws Exception {
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandResult commandResult = new CommandResult(ListCommand.FOUND_THE_FOLLOWING_DISEASE
                + CommandResult.convertListToString(diagnosisModel.getDiseases()));
        assertEquals(commandResult, testDiagnosisModel(diagnosisModel));
    }

    /**
     * Tests diagnosis model
     *
     * @throws Exception
     */
    private CommandResult testDiagnosisModel(DiagnosisModel diagnosisModel) throws Exception {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        CommandHistory commandHistory = new CommandHistory();

        ListCommand cmd = new ListCommand(CMDTYPE_DISEASE, ListCommand.GET_ALL_WORD);

        return cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);
    }

}
