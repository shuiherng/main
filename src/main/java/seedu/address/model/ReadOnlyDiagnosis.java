package seedu.address.model;

import java.util.HashMap;
import java.util.Set;

import javafx.beans.value.ObservableValue;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;


/**
 *
 */
public interface ReadOnlyDiagnosis {

    ObservableValue<HashMap<Disease, Set<Symptom>>> getDiagnosis();
}
