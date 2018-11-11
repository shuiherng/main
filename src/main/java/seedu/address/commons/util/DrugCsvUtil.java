package seedu.address.commons.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Reads through the "dataSetForDrugs.csv" file.
 */

public class DrugCsvUtil {

    //filepath to drug dataset
    private static final String DATASET_PATH = "./src/main/resources/storage/datasetForDrugs.csv";

    private Reader reader;
    private CSVReader csvReader;

    private String keyword; //keyword to be searched for

    public DrugCsvUtil(String keyword) throws IOException {
        this.reader = Files.newBufferedReader(Paths.get(DATASET_PATH));
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
