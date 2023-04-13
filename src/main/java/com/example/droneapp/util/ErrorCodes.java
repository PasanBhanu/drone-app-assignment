package com.example.droneapp.util;

public enum ErrorCodes {
    RECORD_NOT_FOUND("record not found"),
    RECORD_ALREADY_EXISTS("record already exist"),
    INVALID_DATA("invalid request data"),
    INVALID_STATE("model does not fulfil the conditions");

    private final String label;

    ErrorCodes(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
