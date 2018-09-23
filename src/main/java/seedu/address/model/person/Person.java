package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Enumerated Variable to represent person attributes
    private enum PersonProperty {NAME, PHONE, EMAIL, ADDRESS, TAGS}
    private final HashMap<PersonProperty, Object> attributes;


    // Identity fields
    private final ID id;

    /*
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    */

    /**
     *
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.id = new ID();
        this.attributes = new HashMap<>();
        this.attributes.put(PersonProperty.NAME, name);
        this.attributes.put(PersonProperty.PHONE, phone);
        this.attributes.put(PersonProperty.EMAIL, email);
        this.attributes.put(PersonProperty.ADDRESS, address);

        Set<Tag> personTags = new HashSet<>(tags); // adds all tags into here
        this.attributes.put(PersonProperty.TAGS, personTags);

        /*
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        */
    }

    public ID getID() {
        return this.id;
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
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getID().equals(getID());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
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
        return otherPerson.getID().equals(getID());
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
                .append("[ID: ")
                .append(getID())
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
