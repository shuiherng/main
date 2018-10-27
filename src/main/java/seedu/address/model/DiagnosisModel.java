package seedu.address.model;

import java.util.List;
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
     * @return a list of all symptoms.
     */
    List<Symptom> getSymptoms(Disease disease);

    /**
     * Gets all diseases stored in patient book.
     * @return a list all diseases.
     */
    List<Disease> getDiseases();

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
     * @return a list of qualified diseases.
     */
    List<Disease> predictDisease(Set<Symptom> symptoms);

}
