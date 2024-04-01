package com.cse5382.assignment.Exceptions;

import java.sql.SQLException;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import com.cse5382.assignment.Model.PhoneBookResponse;

@ControllerAdvice
public class PhoneBookControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<PhoneBookResponse> handleInvalidData(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        PhoneBookResponse response = new PhoneBookResponse();
        StringBuilder errors = new StringBuilder("Please fix the following error(s) in the payload: ");
        int count = 1;
        
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            for (FieldError err : exception.getBindingResult().getFieldErrors()) {
                String field = err.getField();
                String rejectedValue = (String) err.getRejectedValue();
                String defaultMessage = err.getDefaultMessage();

                errors.append(count + ") ");
                errors.append("'" + field + "' (" + rejectedValue + ") ");
                errors.append(" - ");
                errors.append(defaultMessage);
                errors.append(". ");

                count++;
            }    
        } else {
            ConstraintViolationException exception = (ConstraintViolationException) e;
            errors.append(exception.getMessage());
        }
        
        
        response.setStatusCode(400);
        response.setMessage(errors.toString());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<PhoneBookResponse> handleSQLException(SQLException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        PhoneBookResponse response = new PhoneBookResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL Error");
        SQLiteException underLyingException = (SQLiteException) (e.getCause());
        
        if(underLyingException.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT && underLyingException.getMessage().contains("phoneNumber")) {
            status = HttpStatus.CONFLICT;
            response.setStatusCode(409);
            response.setMessage("The given phone number already belongs to someone else");
        }
        
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(PhoneBookEmptyException.class)
    public ResponseEntity<PhoneBookResponse> handleEmptyPhoneBook(PhoneBookEmptyException e) {
        PhoneBookResponse response = new PhoneBookResponse(HttpStatus.OK.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(PhoneBookEntryNotFoundException.class)
    public ResponseEntity<PhoneBookResponse> handleEntryNotFound(PhoneBookEntryNotFoundException e) {
        PhoneBookResponse response = new PhoneBookResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhoneBookEntryAlreadyExistsException.class)
    public ResponseEntity<PhoneBookResponse> handleDuplicateEntry(PhoneBookEntryAlreadyExistsException e) {
        PhoneBookResponse response = new PhoneBookResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
