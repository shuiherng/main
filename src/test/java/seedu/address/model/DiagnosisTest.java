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

public class DiagnosisTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Diagnosis diagnosis = new Diagnosis();

    private Disease influenza = new Disease("influenza");
    private Symptom lip_smacking = new Symptom("lip smacking");
    private Symptom fever = new Symptom("fever");

    private Disease autism = new Disease("autism");

    private Disease acne = new Disease("acne");
    private Symptom blackhead = new Symptom("blackhead");
    private Symptom whitehead = new Symptom("whitehead");

    @Test
    public void hasDisease() {
        assertTrue(diagnosis.hasDisease(influenza));
        assertFalse(diagnosis.hasDisease(autism));
    }

    @Test
    public void getSymptoms() {

        assertNotNull(diagnosis.getSymptoms(influenza));
        assertTrue(diagnosis.getSymptoms(influenza).contains(lip_smacking));
        assertFalse(diagnosis.getSymptoms(influenza).contains(blackhead));
        assertNotEquals(Collections.emptyList(), diagnosis.getSymptoms(influenza));

        thrown.expect(NullPointerException.class);
        diagnosis.getSymptoms(autism);
    }

    @Test
    public void getDiseases() {
        assertNotEquals(Collections.emptyList(), diagnosis.getDiseases());
        assertTrue(diagnosis.getDiseases().contains(influenza));
        assertFalse(diagnosis.getDiseases().contains(autism));
    }

    @Test
    public void addMatcher() {

        Set<Symptom> symptomSet = new HashSet<>();
        symptomSet.add(blackhead);
        symptomSet.add(whitehead);
        diagnosis.addMatcher(acne, symptomSet);

        assertTrue(diagnosis.hasDisease(acne));
        assertNotEquals(Collections.emptyList(), diagnosis.getSymptoms(acne));
        assertTrue(diagnosis.getSymptoms(acne).contains(blackhead));
        assertFalse(diagnosis.getSymptoms(acne).contains(fever));

    }

    @Test
    public void predictDisease() {
        Set<Symptom> symptomSet = new HashSet<>();
        symptomSet.add(fever);
        symptomSet.add(lip_smacking);
        List<Disease> diseaseList = new ArrayList<>();
        diseaseList.add(influenza);
        assertEquals(diseaseList, diagnosis.predictDisease(symptomSet));

        symptomSet.add(blackhead);
        assertEquals(Collections.emptyList(), diagnosis.predictDisease(symptomSet));
    }
}