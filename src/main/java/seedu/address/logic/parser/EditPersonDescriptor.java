package seedu.address.logic.parser;

import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT;
import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_MAXIMUM_NUMBER_OF_APPOINTMENTS;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.util.MaximumSortedList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.IncomeLevel;
import seedu.address.model.person.Monthly;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;


/**
 * Stores the details to edit the person with. Each non-empty field value will replace the
 * corresponding field value of the person.
 */
public class EditPersonDescriptor {
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private IncomeLevel income;
    private Monthly monthly;
    private Set<Tag> tags;
    private MaximumSortedList<Appointment> appointments;
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
        setIncome(toCopy.income);
        setMonthly(toCopy.monthly);
        setTags(toCopy.tags);
        setAppointments(toCopy.appointments);
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyNonNull(name, phone, email, address, income, monthly, tags, appointments);
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

    /**
     * Sets {@code appointments} to this object's {@code appointments}.
     * A defensive copy of {@code appointments} is used internally.
     */
    public void setAppointments(MaximumSortedList<Appointment> appointments) {
        this.appointments = (appointments != null) ? new MaximumSortedList<>(appointments) : null;
    }

    public Optional<MaximumSortedList<Appointment>> getAppointments() {
        return Optional.ofNullable(appointments);
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
    public void setIncome(IncomeLevel income) {
        this.income = income;
    }
    private Optional<IncomeLevel> getIncome() {
        return Optional.ofNullable(income);
    }

    public void setMonthly(Monthly monthly) {
        this.monthly = monthly;
    }
    public Optional<Monthly> getMonthly() {
        return Optional.ofNullable(monthly);
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

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    public static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        IncomeLevel updatedIncomeLevel = editPersonDescriptor.getIncome().orElse(personToEdit.getIncome());
        Monthly updateMonthly = editPersonDescriptor.getMonthly().orElse(personToEdit.getMonthly());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedIncomeLevel,
                updateMonthly, updatedTags);
    }


    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with appointments added from {@code editPersonDescriptor}.
     */
    public static Person createEditedPersonByAddingAppointments(
            Person personToEdit, EditPersonDescriptor editPersonDescriptor) throws ParseException {
        assert personToEdit != null;

        MaximumSortedList<Appointment> updatedAppointments = personToEdit.getAppointments();

        Optional<Boolean> hasAppointment = editPersonDescriptor.appointments.stream()
                .map(updatedAppointments::contains).reduce((x, y) -> x || y);
        Optional<Boolean> isAppointmentsEdited = editPersonDescriptor.appointments.stream()
                .map(updatedAppointments::add).reduce((x, y) -> x || y);

        if (hasAppointment.isEmpty() || hasAppointment.get()) {
            throw new ParseException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        if (isAppointmentsEdited.isEmpty() || !isAppointmentsEdited.get()) {
            throw new ParseException(MESSAGE_MAXIMUM_NUMBER_OF_APPOINTMENTS);
        }

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        Set<Tag> tags = personToEdit.getTags();
        Monthly monthly = personToEdit.getMonthly();

        IncomeLevel income = personToEdit.getIncome();
        Person newPerson = new Person(name, phone, email, address, income, monthly, tags, updatedAppointments);

        return newPerson;
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * overwritten with appointments from {@code editPersonDescriptor}.
     */
    public static Person createEditedPersonByOverwritingAppointments(Person personToEdit,
                                                                   EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        Monthly monthly = personToEdit.getMonthly();
        Set<Tag> tags = personToEdit.getTags();
        IncomeLevel income = personToEdit.getIncome();
        MaximumSortedList<Appointment> newAppointmentsOnly = editPersonDescriptor.getAppointments().get();
        Person newPerson = new Person(name, phone, email, address, income, monthly, tags, newAppointmentsOnly);

        return newPerson;
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
                && getMonthly().equals(e.getMonthly())
                && getTags().equals(e.getTags())
                && getAppointments().equals(e.getAppointments());
    }
}
