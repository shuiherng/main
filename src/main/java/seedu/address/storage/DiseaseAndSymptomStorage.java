package seedu.address.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.CSVReader;
import seedu.address.model.symptom.Disease;
import seedu.address.model.symptom.Symptom;

/**
 * A class to access/write data of diseases and its related symptoms stored in a csv file.
 */
public class DiseaseAndSymptomStorage {
    private static final String pathStringForCSV = "src/main/resources/storage/datasetForSymptomAndDisease.csv";

    private Set<Disease> diseases;
    private Set<Symptom> symptoms;
    private HashMap<Disease, List<Symptom>> matcher;

    public DiseaseAndSymptomStorage() {
        this.diseases = DiseaseAndSymptomStorage.returnSetOfDiseases();
        this.symptoms = DiseaseAndSymptomStorage.returnSetOfSymptoms();
        this.matcher = DiseaseAndSymptomStorage.readDataFromCsvFile();
        assert this.matcher != null;
    }


    public Set<Disease> getDiseases() {
        return diseases;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public HashMap<Disease, List<Symptom>> getMatcher() {
        return matcher;
    }


    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a HashMap object where key is disease and value is a list of related symptoms.
     */
    private static HashMap<Disease, List<Symptom>> readDataFromCsvFile() {
        try {
            HashMap<Disease, List<Symptom>> diseaseSymptomMatcher = new HashMap<>();
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
                diseaseSymptomMatcher.put(disease, symptoms);
            }
            return diseaseSymptomMatcher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write data to datasetForSymptomAndDisease.csv.
     *
     * @param data A String object which contains a disease and its related symptoms.
     * @return a new DiseaseAndSymptomStorage object with updated data.
     */
    private static DiseaseAndSymptomStorage writeDataFromCsvFile(String data) {
        try {
            String filePath = new File(pathStringForCSV)
                    .getAbsolutePath();
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);

            fileWriter.append(data);

            fileWriter.flush();
            fileWriter.close();

            return new DiseaseAndSymptomStorage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a set of diseases in the csv file storage.
     */
    private static Set<Disease> returnSetOfDiseases() {
        HashMap<Disease, List<Symptom>> diseaseSymptomMatcher = DiseaseAndSymptomStorage.readDataFromCsvFile();
        return diseaseSymptomMatcher.keySet();
    }

    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a set of symptoms in the csv file storage.
     */
    private static Set<Symptom> returnSetOfSymptoms() {
        HashMap<Disease, List<Symptom>> diseaseSymptomMatcher = DiseaseAndSymptomStorage.readDataFromCsvFile();
        Collection<List<Symptom>> values = diseaseSymptomMatcher.values();
        Set<Symptom> symptoms = new HashSet<>();
        for (List<Symptom> symptomList : values) {
            symptoms.addAll(symptomList);
        }
        return symptoms;
    }
}
