package com.example.droneapp.exception;

import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.controller.response.ErrorResponse;
import com.example.droneapp.controller.response.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected <T extends ErrorResponse, E extends MethodArgumentNotValidException> ResponseEntity<T> handleValidationExceptions(E ex) {
        log.info("Request validation failed");

        ArrayList<ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            validationErrors.add(new ValidationError(((FieldError) error).getField(), error.getDefaultMessage()));
        });

        ValidationErrorResponse response = new ValidationErrorResponse(400, "Request validation failed.", validationErrors);
        return new ResponseEntity<T>((T) response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseValidationException.class)
    protected <T extends ErrorResponse> ResponseEntity<T> handleDatabaseValidationException(DatabaseValidationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse(ex.getStatus(), ex.getMessage(), ex.getValidationErrors());
        return new ResponseEntity<T>((T) response, ex.getHttpStatus());
    }

    @ExceptionHandler(LogicViolationException.class)
    protected <T extends ErrorResponse> ResponseEntity<T> handleLogicViolationException(LogicViolationException ex) {
        ErrorResponse response = new ErrorResponse(ex.getStatus(), ex.getMessage(), ex.getErrors());
        return new ResponseEntity<T>((T) response, ex.getHttpStatus());
    }

    @ExceptionHandler(ServiceException.class)
    protected <T extends ErrorResponse> ResponseEntity<T> handleServiceException(ServiceException ex) {
        ErrorResponse response = new ErrorResponse(ex.getStatus(), ex.getMessage(), ex.getErrors());
        return new ResponseEntity<T>((T) response, ex.getHttpStatus());
    }
}
