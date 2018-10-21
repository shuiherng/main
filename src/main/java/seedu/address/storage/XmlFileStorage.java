package seedu.address.storage;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores addressbook data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveAddressBookToFile(Path file, XmlSerializableAddressBook addressBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, addressBook);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableAddressBook loadAddressBookFromSaveFile(Path file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableAddressBook.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * saves schedule to file given by pat
     * @param file file path
     * @param schedule schedule to be saved
     * @throws FileNotFoundException
     */
    public static void saveScheduleToFile(Path file, XmlSerializableSchedule schedule)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, schedule);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns schedule in the file or an empty schedule
     */
    public static XmlSerializableSchedule loadScheduleFromSaveFile(Path file) throws DataConversionException,
                                                                        FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableSchedule.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }
}
