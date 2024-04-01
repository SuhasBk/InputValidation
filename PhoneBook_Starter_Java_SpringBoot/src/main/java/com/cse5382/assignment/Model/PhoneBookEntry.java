package com.cse5382.assignment.Model;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cse5382.assignment.Service.PhoneBookServiceImpl;
import com.cse5382.assignment.Util.AppConstants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "phonebook", daoClass = PhoneBookServiceImpl.class)
public class PhoneBookEntry {

    public static final String PHONE_NUMBER_FIELD_NAME = "phoneNumber";
    public static final String NAME_FIELD_NAME = "name";

    @DatabaseField(id = true, columnName = NAME_FIELD_NAME)
    @NotNull
    @Pattern(regexp = AppConstants.NAME_REGEX, message = "Please enter a valid name")
    private String name;

    @DatabaseField(unique = true, columnName = PHONE_NUMBER_FIELD_NAME, canBeNull = false)
    @NotNull
    @Pattern(regexp = AppConstants.PHONE_NUMBER_REGEX, message = "Please enter a valid phone number")
    private String phoneNumber;

    public PhoneBookEntry() {
    }

    public PhoneBookEntry(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public PhoneBookEntry name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneBookEntry phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneBookEntry phoneBookEntry = (PhoneBookEntry) o;
        return Objects.equals(this.name, phoneBookEntry.name) &&
                Objects.equals(this.phoneNumber, phoneBookEntry.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PhoneBookEntry {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
