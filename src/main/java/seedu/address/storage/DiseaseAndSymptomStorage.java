package seedu.address.storage;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;

public class DiseaseAndSymptomStorage {
    private Set<String> diseases;
    private Set<String> symptoms;
    private HashMap<String, List<String>> matcher;

    public DiseaseAndSymptomStorage() {
        this.diseases = DiseaseAndSymptomStorage.returnSetOfDiseases();
        this.symptoms = DiseaseAndSymptomStorage.returnSetOfDiseases();
        this.matcher = DiseaseAndSymptomStorage.readDataFromCSVFile();
        assert this.matcher != null;
    }

    public static void main(String[] args) {
        DiseaseAndSymptomStorage diseaseAndSymptomStorage = new DiseaseAndSymptomStorage();
        for (String b : diseaseAndSymptomStorage.diseases
        ) {
            System.out.println(b);
        }
    }

    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a HashMap object where key is disease and value is a list of related symptoms.
     */
    private static HashMap<String, List<String>> readDataFromCSVFile() {
        try {
            HashMap<String, List<String>> diseaseSymptomMatcher = new HashMap<>();
            String filePath = new File("src/main/java/seedu/address/storage/datasetForSymptomAndDisease.csv").getAbsolutePath();
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String disease = nextRecord[0];
                nextRecord = ArrayUtils.remove(nextRecord, 0);
                List<String> symptoms = Arrays.asList(nextRecord);
                diseaseSymptomMatcher.put(disease, symptoms);
            }
            return diseaseSymptomMatcher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a set of diseases in the csv file storage.
     */
    private static Set<String> returnSetOfDiseases() {
        HashMap<String, List<String>> diseaseSymptomMatcher = DiseaseAndSymptomStorage.readDataFromCSVFile();
        return diseaseSymptomMatcher.keySet();
    }

    /**
     * Read data from datasetForSymptomAndDisease.csv.
     *
     * @return a set of symptoms in the csv file storage.
     */
    private static Set<String> returnSetOfSymptoms() {
        HashMap<String, List<String>> diseaseSymptomMatcher = DiseaseAndSymptomStorage.readDataFromCSVFile();
        Collection<List<String>> values = diseaseSymptomMatcher.values();
        Set<String> symptoms = new HashSet<>();
        for (List<String> symptomList : values) {
            symptoms.addAll(symptomList);
        }
        return symptoms;
    }
}
