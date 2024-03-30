package com.cse5382.assignment.Exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cse5382.assignment.Model.ErrorResponse;

@ControllerAdvice
public class PhoneBookControllerAdvice {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL Error: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PhoneBookEmptyException.class)
    public ResponseEntity<ErrorResponse> handleEmptyPhoneBook(PhoneBookEmptyException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhoneBookEntryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntryNotFound(PhoneBookEntryNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhoneBookEntryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntry(PhoneBookEntryAlreadyExistsException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
