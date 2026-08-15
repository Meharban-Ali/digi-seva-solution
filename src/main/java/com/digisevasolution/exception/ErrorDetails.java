package com.digisevasolution.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private List<String> errors;

    public ErrorDetails() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDetails(LocalDateTime timestamp, String message, String details, List<String> errors) {
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.message = message;
        this.details = details;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
