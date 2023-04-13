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
public class LogicViolationException extends RuntimeException{
    private Integer status = 406;
    private HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
    private String message = "Internal server error";
    private List<ValidationError> errors = null;

    public LogicViolationException(ErrorCodes message, List<ValidationError> errors) {
        this.message = message.getLabel();
        this.errors = errors;
    }
}
