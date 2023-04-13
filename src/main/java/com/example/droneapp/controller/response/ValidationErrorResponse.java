package com.example.droneapp.controller.response;

import com.example.droneapp.controller.model.ValidationError;
import lombok.Data;

import java.util.List;

@Data
public class ValidationErrorResponse extends ErrorResponse {
    public ValidationErrorResponse(Integer status, String message, List<ValidationError> errors) {
        this.setStatus(status);
        this.setMessage(message);
        this.errors = errors;
    }

    private List<ValidationError> errors;
}
