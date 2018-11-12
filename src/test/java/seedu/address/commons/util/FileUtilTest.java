package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class FileUtilTest {


    @Test
    public void isValidPath() {
        // valid path
        assertTrue(FileUtil.isValidPath("valid/file/path"));

        // invalid path
        assertFalse(FileUtil.isValidPath("a\0"));

        // null path -> throws NullPointerException
        Assert.assertThrows(NullPointerException.class, () -> FileUtil.isValidPath(null));
    }

    @Test
    public void readFromCsvFile() {
        Path path = Paths.get("daf\231/frf");

        Assert.assertThrows(NoSuchFileException.class, () -> FileUtil.readFromCsvFile(path));

    }

    @Test
    public void writeToCsv() {
        Path path = Paths.get("no/such/path");

        String empty = "";

        Assert.assertThrows(NoSuchFileException.class, () -> FileUtil.writeToCsvFile(path, empty));

    }
}
