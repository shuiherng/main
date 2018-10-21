package seedu.address.model;

import java.util.Set;

import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;

/**
 * The API of the DiagnosisModel component.
 */
public interface DiagnosisModel {

    /**
     * Checks if a disease is already contained in the database.
     *
     * @param disease disease for checking.
     * @return a boolean value for the check.
     */
    boolean hasDisease(Disease disease);

    /**
     * Gets all related symptoms of a disease.
     *
     * @param disease disease input.
     * @return a set of all symptoms.
     */
    Set<Symptom> getSymptoms(Disease disease);

    /**
     * Adds a disease and its related symptoms into the database
     *
     * @param disease disease input.
     * @param symptoms related symptoms.
     */
    void addMatcher(Disease disease, Set<Symptom> symptoms);

    /**
     * Predicts a disease with a given set of symptoms.
     *
     * @param symptoms symptoms input.
     * @return a set of qualified diseases.
     */
    Set<Disease> predictDisease(Set<Symptom> symptoms);

}
