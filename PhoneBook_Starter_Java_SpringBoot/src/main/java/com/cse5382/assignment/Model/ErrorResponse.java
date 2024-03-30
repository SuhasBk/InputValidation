package com.cse5382.assignment.Model;

public class ErrorResponse {
    
    private int statusCode;
    private String error;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.error = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
