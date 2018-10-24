package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;

/**
 * Represents the DiagnosisModel in address book.
 */
public class DiagnosisModelManager implements DiagnosisModel {

    private final Diagnosis diagnosis;

    public DiagnosisModelManager() {
        diagnosis = new Diagnosis();
    }

    @Override
    public boolean hasDisease(Disease disease) {
        requireNonNull(disease);
        return diagnosis.hasDisease(disease);
    }

    @Override
    public List<Symptom> getSymptoms(Disease disease) {
        requireNonNull(disease);
        return diagnosis.getSymptoms(disease);
    }

    @Override
    public List<Disease> getDiseases() {
        return diagnosis.getDiseases();
    }

    @Override
    public void addMatcher(Disease disease, Set<Symptom> symptoms) {
        requireNonNull(disease);
        requireAllNonNull(symptoms);
        diagnosis.addMatcher(disease, symptoms);
    }

    @Override
    public List<Disease> predictDisease(Set<Symptom> symptoms) {
        requireAllNonNull(symptoms);
        return diagnosis.predictDisease(symptoms);
    }
}
