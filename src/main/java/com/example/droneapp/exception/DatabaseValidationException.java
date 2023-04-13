package com.example.droneapp.exception;

import com.example.droneapp.controller.model.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseValidationException extends RuntimeException {
    private Integer status = 400;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "Database validation failed";
    private ArrayList<ValidationError> validationErrors = null;

    public DatabaseValidationException(Integer status, HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.status = status;
        this.message = message;
    }
}
