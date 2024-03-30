package com.cse5382.assignment.Exceptions;

public class PhoneBookEntryAlreadyExistsException extends RuntimeException {

    public PhoneBookEntryAlreadyExistsException() {
        super("The provided name/number combination already exists!");
    }
    
}
