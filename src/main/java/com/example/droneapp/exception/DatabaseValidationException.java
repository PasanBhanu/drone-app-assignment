package com.example.droneapp.exception;

import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.util.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseValidationException extends RuntimeException {
    private Integer status = 400;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "Database validation failed";
    private List<ValidationError> errors = null;

    public DatabaseValidationException(ErrorCodes message, List<ValidationError> errors) {
        this.message = message.getLabel();
        this.errors = errors;
    }
}
