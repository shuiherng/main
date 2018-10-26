package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    /**
     * Enumerated Variable to represent person attributes
     */
    private enum PersonProperty {
        NAME, PHONE, EMAIL, ADDRESS, TAGS
    }

    private final HashMap<PersonProperty, Object> attributes;
    private boolean exists;

    // Identity fields
    private final PersonId id;

    /*
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    */

    /**
     * Every field must be present and not null.
     */

    public Person(PersonId personId, Name name, Phone phone, Email email,
                  Address address, boolean exists, Set<Tag> tags) {
        requireAllNonNull(personId, name, phone, email, address, tags);
        this.id = personId;
        this.exists = exists;
        this.attributes = new HashMap<>();
        this.attributes.put(PersonProperty.NAME, name);
        this.attributes.put(PersonProperty.PHONE, phone);
        this.attributes.put(PersonProperty.EMAIL, email);
        this.attributes.put(PersonProperty.ADDRESS, address);

        Set<Tag> personTags = new HashSet<>(tags); // adds all tags into here
        this.attributes.put(PersonProperty.TAGS, personTags);
    }

    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(new PersonId(), name, phone, email, address, true, tags);
    }


    public PersonId getId() {
        return this.id;
    }

    public boolean getExists() {
        return this.exists;
    }

    public Name getName() {
        return (Name) this.attributes.get(PersonProperty.NAME);
    }

    public Phone getPhone() {
        return (Phone) this.attributes.get(PersonProperty.PHONE);
    }

    public Email getEmail() {
        return (Email) this.attributes.get(PersonProperty.EMAIL);
    }

    public Address getAddress() {
        return (Address) this.attributes.get(PersonProperty.ADDRESS);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @SuppressWarnings("unchecked")
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet((Set<Tag>) this.attributes.get(PersonProperty.TAGS));
    }

    /**
     * Performs soft-remove by marking the person as non-existent
     * <p>
     * Developer note: when writing unit tests, remember to do {@code undelete()} after performing this operation.
     */
    public void delete() {
        if (!this.exists) {
            throw new PersonNotFoundException();
        }
        this.exists = false;
    }

    /**
     * Sets this person as exists.
     * <p>
     * Developer note: when writing unit tests, remember to {@code delete} after performing this operation.
     */
    public void undelete() {
        if (this.exists) {
            throw new PersonNotFoundException();
        }
        this.exists = true;
    }

    /**
     * Returns true if both persons have the same ID, even if they have different attributes.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getId().equals(getId());
    }

    /**
     * Returns true if both persons have the same ID.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getId().equals(getId());
        /*
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getTags().equals(getTags());
         */
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(getExists() ? "[Exists]" : "[Deleted]")
                .append("[PersonId: ")
                .append(getId())
                .append("]")
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
