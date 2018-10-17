package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Optional;

import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.ScheduleChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlySchedule;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, ScheduleStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    @Override
    Path getScheduleFilePath();

    @Override
    Optional<ReadOnlySchedule> readSchedule() throws DataConversionException, IOException, ParseException;

    @Override
    void saveSchedule(ReadOnlySchedule schedule) throws IOException;


    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(AddressBookChangedEvent abce);

    /**
     * Saves the current version of the Schedule to the hard disk.
     *  Creates the data file if it is missing.
     *  Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleScheduleChangedEvent(ScheduleChangedEvent sce);
}
