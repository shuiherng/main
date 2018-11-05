package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DiagnosisModelManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final DiagnosisModelManager diagnosisModelManager = new DiagnosisModelManager();

    private Disease melanoma = new Disease("melanoma");
    private Symptom paraparesis = new Symptom("paraparesis");
    private Symptom pruritus = new Symptom("pruritus");

    private Disease autism = new Disease("autism");

    private Disease stroke = new Disease("stroke");
    private Symptom arm_weakness = new Symptom("arm weakness");
    private Symptom confusion = new Symptom("confusion");

    @Test
    public void hasDisease() {
        diagnosisModelManager.hasDisease(melanoma);
        assertFalse(diagnosisModelManager.hasDisease(autism));
    }

    @Test
    public void getSymptoms() {
        assertNotNull(diagnosisModelManager.getSymptoms(melanoma));
        assertTrue(diagnosisModelManager.getSymptoms(melanoma).contains(paraparesis));
        assertFalse(diagnosisModelManager.getSymptoms(melanoma).contains(arm_weakness));
        assertNotEquals(Collections.emptyList(), diagnosisModelManager.getSymptoms(melanoma));

        thrown.expect(NullPointerException.class);
        diagnosisModelManager.getSymptoms(autism);
    }

    @Test
    public void getDiseases() {
        assertNotEquals(Collections.emptyList(), diagnosisModelManager.getDiseases());
        assertTrue(diagnosisModelManager.getDiseases().contains(melanoma));
        assertFalse(diagnosisModelManager.getDiseases().contains(autism));
    }

    @Test
    public void addMatcher() {
        Set<Symptom> symptomSet = new HashSet<>();
        symptomSet.add(arm_weakness);
        symptomSet.add(confusion);
        diagnosisModelManager.addMatcher(stroke, symptomSet);

        assertTrue(diagnosisModelManager.hasDisease(stroke));
        assertNotEquals(Collections.emptyList(), diagnosisModelManager.getSymptoms(stroke));
        assertTrue(diagnosisModelManager.getSymptoms(stroke).contains(arm_weakness));
        assertFalse(diagnosisModelManager.getSymptoms(stroke).contains(pruritus));
    }

    @Test
    public void predictDisease() {
        Set<Symptom> symptomSet = new HashSet<>();
        symptomSet.add(pruritus);
        symptomSet.add(paraparesis);
        List<Disease> diseaseList = new ArrayList<>();
        diseaseList.add(melanoma);
        assertEquals(diseaseList, diagnosisModelManager.predictDisease(symptomSet));

        symptomSet.add(arm_weakness);
        assertEquals(Collections.emptyList(), diagnosisModelManager.predictDisease(symptomSet));
    }
}