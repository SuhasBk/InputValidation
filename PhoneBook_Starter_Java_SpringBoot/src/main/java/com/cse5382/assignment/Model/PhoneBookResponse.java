package com.cse5382.assignment.Model;

public class PhoneBookResponse {
    
    private int statusCode;
    private String message;

    public PhoneBookResponse() {
        this.statusCode = 200;
        this.message = "OK";
    }

    public PhoneBookResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String error) {
        this.message = error;
    }
}
