package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.PersonCliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.ScheduleEventCliSyntax.*;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_PERSONS;

import java.util.*;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.Pair;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.EventId;
import seedu.address.model.event.ScheduleEvent;
import seedu.address.model.event.exceptions.ScheduleEventNotFoundException;
import seedu.address.model.person.*;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    private static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";

    // private final Index index;
    // private final EditPersonDescriptor editPersonDescriptor;
    private final String cmdType;
    private final String target;
    private final ArgumentMultimap argMultimap;

    /**
     * @param cmdType Indicates if the command is to edit a patient or an appointment.
     * @param target Patient or Appointment ID.
     * @param argMultimap arguments
     */
    public EditCommand(String cmdType, String target, ArgumentMultimap argMultimap) {
        this.cmdType = cmdType;
        this.target = target;
        this.argMultimap = argMultimap;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        requireNonNull(diagnosisModel);

        if (cmdType.equals("patient")) {
            if (!PersonId.isValidId(cmdType)) {
                throw new CommandException("Incorrect format for patient.");
            }
            EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                try {
                    editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
                } catch (ParseException e) {
                    throw new CommandException("Incorrect format for name.", e);
                }
            }
            if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
                try {
                    editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
                } catch (ParseException e) {
                    throw new CommandException("Incorrect format for phone.", e);
                }
            }
            if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
                try {
                    editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
                } catch (ParseException e) {
                    throw new CommandException("Incorrect format for email.", e);
                }
            }
            if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
                try {
                    editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
                } catch (ParseException e) {
                    throw new CommandException("Incorrect format for address.", e);
                }
            }
            try {
                parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
            } catch (ParseException e) {
                throw new CommandException("Invalid tag format.");
            }
            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new CommandException(EditCommand.MESSAGE_NOT_EDITED);
            }

            // Edit the person
            try {
                Person personToEdit = addressBookModel.getPersonById(new PersonId(target));
                Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

                addressBookModel.updatePerson(personToEdit, editedPerson);
                addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));

            } catch (PersonNotFoundException e) {
                throw new CommandException("Could not locate unique person with given ID.");
            }

        } else if (cmdType.equals("appointment")) {
            if (!EventId.isValidId(cmdType)) {
                throw new CommandException("Incorrect format for event.");
            }
            EditScheduleEventDescriptor editScheduleEventDescriptor = new EditScheduleEventDescriptor();
            if (argMultimap.getValue(PREFIX_PERSON).isPresent()) {
                editScheduleEventDescriptor.setPerson(ParserUtil.parsePersonId(
                        argMultimap.getValue(PREFIX_PERSON).get()));
            }
            if (argMultimap.getValue(PREFIX_DATETIME).isPresent()) {
                try {
                    Pair<Calendar> newDatetime = new DateTimeParser().parseTimeSlot(
                            argMultimap.getValue(PREFIX_DATETIME).get());
                    editScheduleEventDescriptor.setDate(newDatetime);
                } catch (ParseException e) {
                    throw new CommandException("Invalid format for date input");
                }
            }
            if (argMultimap.getValue(PREFIX_DETAILS).isPresent()) {
                editScheduleEventDescriptor.setDetails(argMultimap.getValue(PREFIX_DETAILS).get());
            }
            try {
                parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAGS)).ifPresent(editScheduleEventDescriptor::setTags);
            } catch (ParseException e) {
                throw new CommandException("Invalid format for tags.");
            }

            // Edit the event
            try {
                ScheduleEvent eventToEdit = scheduleModel.getEventById(new EventId(target));
                ScheduleEvent editedEvent = createEditedEvent(eventToEdit, editScheduleEventDescriptor);

                scheduleModel.updateEvent(eventToEdit, editedEvent);
                scheduleModel.updateFilteredEventList(ScheduleModel.PREDICATE_SHOW_ALL_SCHEDULE_EVENTS);
                return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));

            } catch (ScheduleEventNotFoundException e) {
                throw new CommandException("Could not locate unique event with given ID.");
            }

        } else {
            throw new CommandException("Unexpected values for cmdType: should have been caught in parser.");
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        PersonId personId = personToEdit.getId();
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        boolean exists = personToEdit.getExists();
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(personId, updatedName, updatedPhone, updatedEmail,
                updatedAddress, exists, updatedTags);
    }

    /**
     * Creates and returns a {@code ScheduleEvent} with the details of {@code eventToEdit}
     * edited with {@code editScheduleEventDescriptor}
     */
    private static ScheduleEvent createEditedEvent(ScheduleEvent eventToEdit,
                                                   EditScheduleEventDescriptor editScheduleEventDescriptor) {
        assert eventToEdit != null;

        EventId eventId = eventToEdit.getId();
        Pair<Calendar> updatedDate = editScheduleEventDescriptor.getDate().orElse(
                eventToEdit.getDate());
        PersonId updatedPerson = editScheduleEventDescriptor.getPerson().orElse(
                eventToEdit.getPersonId());
        String updatedDetails = editScheduleEventDescriptor.getDetails().orElse(
                eventToEdit.getDetails());
        Set<Tag> updatedTags = editScheduleEventDescriptor.getTags().orElse(
                eventToEdit.getTags());

        return new ScheduleEvent(eventId, updatedDate, updatedPerson, updatedDetails,
                updatedTags);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return cmdType.equals(e.cmdType)
                && target.equals(e.target)
                && argMultimap.equals(e.argMultimap);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }



        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }

    /**
     * Stores the details to edit the schedule event with. Each non-empty field value will replace the
     * corresponding field value of the schedule event.
     */
    public static class EditScheduleEventDescriptor {
        private PersonId personId;
        private Pair<Calendar> date;
        private String details;
        private Set<Tag> tags;

        public EditScheduleEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditScheduleEventDescriptor(EditScheduleEventDescriptor toCopy) {
            setPerson(toCopy.personId);
            setDate(toCopy.date);
            setDetails(toCopy.details);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(personId, date, details, tags);
        }

        public void setPerson(PersonId personId) { this.personId = personId; }

        public Optional<PersonId> getPerson() {
            return Optional.ofNullable(personId);
        }

        public void setDate(Pair<Calendar> date) {
            this.date = date;
        }

        public Optional<Pair<Calendar>> getDate() {
            return Optional.ofNullable(date);
        }

        public void setDetails(String details) { this.details = details; }

        public Optional<String> getDetails() {
            return Optional.ofNullable(details);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }



        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditScheduleEventDescriptor)) {
                return false;
            }

            // state check
            EditScheduleEventDescriptor e = (EditScheduleEventDescriptor) other;

            return getPerson().equals(e.getPerson())
                    && getDate().equals(e.getDate())
                    && getDetails().equals(e.getDetails())
                    && getTags().equals(e.getTags());
        }
    }
}
