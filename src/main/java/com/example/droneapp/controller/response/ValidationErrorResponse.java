package com.example.droneapp.controller.response;

import com.example.droneapp.controller.model.ValidationError;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ValidationErrorResponse extends ErrorResponse {
    public ValidationErrorResponse(Integer status, String message, ArrayList<ValidationError> validationErrors) {
        this.setStatus(status);
        this.setMessage(message);
        this.validationErrors = validationErrors;
    }

    private ArrayList<ValidationError> validationErrors;
}
