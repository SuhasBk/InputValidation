package com.cse5382.assignment.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class PhoneBookUser {

    public static final String USERNAME_FIELD_NAME = "username";
    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String ROLE_FIELD_NAME = "role";

    @DatabaseField(id = true, columnName = USERNAME_FIELD_NAME)
    String username;
    @DatabaseField(columnName = PASSWORD_FIELD_NAME, canBeNull = false)
    String password;
    @DatabaseField(columnName = ROLE_FIELD_NAME, canBeNull = false, defaultValue = "R")
    String role;

    public PhoneBookUser() {

    }

    public PhoneBookUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "PhoneBookUser [username=" + username + ", role=" + role + "]";
    }
    
}
