package seedu.address.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import seedu.address.MainApp;

/**
 * Reads through the "dataSetForDrugs.csv" file.
 */

public class DrugCsvUtil {

    //filepath to drug dataset
    private static final String DATASET_PATH = "/storage/datasetForDrugs.csv";

    private Reader reader;
    private CSVReader csvReader;

    private String keyword; //keyword to be searched for

    public DrugCsvUtil(String keyword) throws IOException {
        InputStream inputStream = MainApp.class
                .getResourceAsStream(DATASET_PATH);
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        this.keyword = keyword;
    }

    /**
     * Returns entries from the drug database one at a time, which match the given keyword.
     */
    public String[] nextMatchingEntry() throws IOException {
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            if (nextRecord[1].toLowerCase().contains(keyword)) {
                return nextRecord;
            }
        }
        return null;
    }
}
