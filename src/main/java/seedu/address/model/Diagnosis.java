package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Wraps all data at the diagnosis level.
 */
public class Diagnosis {

    private static final String pathStringForCSV = "src/main/resources/storage/datasetForSymptomAndDisease.csv";
    private HashMap<Disease, Set<Symptom>> matcher;


    public Diagnosis() {
        matcher = Diagnosis.readDataFromCsvFile();
        requireNonNull(matcher);
    }

    /**
     * Checks if a disease is already contained in the database.
     *
     * @param disease disease for checking.
     * @return a boolean value for the check.
     */
    public boolean hasDisease(Disease disease) {
        requireNonNull(disease);
        Set<Disease> diseases = this.matcher.keySet();
        return diseases.contains(disease);
    }

    /**
     * Gets all related symptoms of a disease.
     *
     * @param disease disease input.
     * @return a list of all symptoms.
     */
    public List<Symptom> getSymptoms(Disease disease) {
        requireNonNull(disease);
        Set<Symptom> symptoms = matcher.get(disease);
        return new ArrayList<>(symptoms);
    }

    /**
     * Gets all diseases stored in patient book.
     *
     * @return a list all diseases.
     */
    public List<Disease> getDiseases() {
        Set<Disease> diseases = matcher.keySet();
        return new ArrayList<>(diseases);
    }

    /**
     * Adds a disease and its related symptoms into the database
     *
     * @param disease  disease input.
     * @param symptoms related symptoms.
     */
    public void addMatcher(Disease disease, Set<Symptom> symptoms) {
        requireNonNull(disease);
        requireAllNonNull(symptoms);
        boolean hasDisease = this.hasDisease(disease);
        if (hasDisease) {
            //todo: prompt user.
        }
        this.matcher = writeDataFromCsvFile(disease, symptoms);
    }

    /**
     * Predicts a disease with a given set of symptoms.
     *
     * @param symptoms symptoms input.
     * @return a list of qualified diseases.
     */
    public List<Disease> predictDisease(Set<Symptom> symptoms) {
        requireAllNonNull(symptoms);
        return null;
    }

    /**
     * Gets the data from CSV storage file.
     *
     * @return a HashMap object which its key is the disease and value is its related symptoms.
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

    /**
     * Writes new data to CSV storage file.
     *
     * @return a new ashMap object which its key is the disease and value is its related symptoms.
     */
    private static HashMap<Disease, Set<Symptom>> writeDataFromCsvFile(Disease disease, Set<Symptom> symptoms) {
        try {
            String filePath = new File(pathStringForCSV)
                    .getAbsolutePath();
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);


            String data = Diagnosis.stringConverter(disease.toString(), symptoms);
            fileWriter.append(data);

            fileWriter.flush();
            fileWriter.close();

            return Diagnosis.readDataFromCsvFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formats the string with disease and related symptoms so that it can be written to CSV file.
     *
     * @param diseaseString
     * @param symptoms
     * @return
     */
    private static String stringConverter(String diseaseString, Set<Symptom> symptoms) {
        diseaseString = diseaseString.concat(",");
        for (Symptom symptom : symptoms) {
            diseaseString = diseaseString.concat(symptom.toString());
            diseaseString = diseaseString.concat(",");
        }
        diseaseString = diseaseString.substring(0, diseaseString.length() - 1);
        diseaseString = diseaseString.concat("\n");
        return diseaseString;
    }
}
