package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.AddressBookModel;
import seedu.address.model.person.Person;

/**
 * Contains helper methods to set up {@code AddressBookModel} for testing.
 */
public class ModelHelper {
    private static final Predicate<Person> PREDICATE_MATCHING_NO_PERSONS = unused -> false;

    /**
     * Updates {@code addressBookModel}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(AddressBookModel addressBookModel, List<Person> toDisplay) {
        Optional<Predicate<Person>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        addressBookModel.updateFilteredPersonList(predicate.orElse(PREDICATE_MATCHING_NO_PERSONS));
    }

    /**
     * @see ModelHelper#setFilteredList(AddressBookModel, List)
     */
    public static void setFilteredList(AddressBookModel addressBookModel, Person... toDisplay) {
        setFilteredList(addressBookModel, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<Person> getPredicateMatching(Person other) {
        return person -> person.equals(other);
    }
}
