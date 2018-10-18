package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.CSVReader;

import javafx.beans.value.ObservableValue;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;


/**
 *
 */
public class Diagnosis implements ReadOnlyDiagnosis {

    private static final String pathStringForCSV = "src/main/resources/storage/datasetForSymptomAndDisease.csv";
    private HashMap<Disease, Set<Symptom>> matcher;


    public Diagnosis() {
        matcher = Diagnosis.readDataFromCsvFile();
    }

    public Diagnosis(ReadOnlyDiagnosis toBeCopied) {
        this();
        //resetData(toBeCopied);
    }

    public void setDiagnosis (HashMap<Disease, Set<Symptom>> matcher) {
        this.matcher = matcher;
    }

    public void resetData(ReadOnlyDiagnosis newData) {
        requireNonNull(newData);

        setDiagnosis(newData.getDiagnosis());
    }

    /**
     * @return
     */
    private static HashMap<Disease, Set<Symptom>> readDataFromCsvFile() {

        try {
            HashMap<Disease, Set<Symptom>> diseaseSymptomMatcher = new HashMap<>();
            String filePath = new File(pathStringForCSV)
                    .getAbsolutePath();
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                Disease disease = new Disease(nextRecord[0]);
                nextRecord = ArrayUtils.remove(nextRecord, 0);
                List<String> symptomsList = Arrays.asList(nextRecord);
                List<Symptom> symptoms = symptomsList.stream().map(x -> new Symptom(x)).collect(Collectors.toList());
                HashSet<Symptom> symptoms1 = new HashSet<>();
                symptoms1.addAll(symptoms);
                diseaseSymptomMatcher.put(disease, symptoms1);
            }
            return diseaseSymptomMatcher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ObservableValue<HashMap<Disease, Set<Symptom>>> getDiagnosis() {
        return null;
    }
}
