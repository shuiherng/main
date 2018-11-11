package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.parser.DiseaseMatcherCliSyntax.PREFIX_SYMPTOM;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.AddressBookModelManager;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.DiagnosisModelManager;
import seedu.address.model.ScheduleModel;
import seedu.address.model.ScheduleModelManager;
import seedu.address.model.symptom.Symptom;

public class PredictCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseDisease_success() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_SYMPTOM + "fever" + " ");
        sb.append(PREFIX_SYMPTOM + "blackout" + " ");
        Set<Symptom> symptomSet = new HashSet<>();
        symptomSet.add(new Symptom("fever"));
        symptomSet.add(new Symptom("blackout"));
        DiagnosisModel diagnosisModel = new DiagnosisModelManager();
        CommandResult commandResult = new CommandResult(PredictCommand.FOUND_DISEASE
                + CommandResult.convertListToString(diagnosisModel.predictDisease(symptomSet)));
        assertEquals(commandResult, testDiagnosisModel(diagnosisModel, sb.toString()));
    }

    @Test
    public void parseDisease_noSuchDisease() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(PredictCommand.DISEASE_NOT_FOUND);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_SYMPTOM + "cry" + " ");
        testDiagnosisModel(new DiagnosisModelManager(), sb.toString());
    }

    @Test
    public void parseDisease_noSymptomValue() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(PredictCommand.EMPTY_SYMPTOM_ERROR);
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_SYMPTOM + " ");
        testDiagnosisModel(new DiagnosisModelManager(), sb.toString());
    }

    /**
     * Tests diagnosis model
     *
     * @throws Exception
     */
    private CommandResult testDiagnosisModel(DiagnosisModel diagnosisModel, String toAdd) throws Exception {
        AddressBookModel addressBookModel = new AddressBookModelManager();
        ScheduleModel scheduleModel = new ScheduleModelManager();
        CommandHistory commandHistory = new CommandHistory();

        PredictCommand cmd = new PredictCommand(toAdd);

        return cmd.execute(addressBookModel, scheduleModel, diagnosisModel, commandHistory);
    }
}
