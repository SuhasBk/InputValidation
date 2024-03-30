package com.cse5382.assignment.Exceptions;

public class PhoneBookEntryNotFoundException extends RuntimeException {

    public PhoneBookEntryNotFoundException() {
        super("An entry for given name or phoneNumber was not found!");
    }
    
}
