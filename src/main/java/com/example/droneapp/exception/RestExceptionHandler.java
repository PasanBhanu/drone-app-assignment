package com.example.droneapp.exception;

import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.controller.response.ErrorResponse;
import com.example.droneapp.controller.response.ValidationErrorResponse;
import com.example.droneapp.util.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            validationErrors.add(new ValidationError(((FieldError) error).getField(), error.getDefaultMessage()));
        });
        ValidationErrorResponse response = new ValidationErrorResponse(400, ErrorCodes.INVALID_DATA.getLabel(), validationErrors);

        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(DatabaseValidationException.class)
    protected <T extends ErrorResponse> ResponseEntity<T> handleDatabaseValidationException(DatabaseValidationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse(ex.getStatus(), ex.getMessage(), ex.getErrors());
        return new ResponseEntity<T>((T) response, ex.getHttpStatus());
    }

    @ExceptionHandler(LogicViolationException.class)
    protected <T extends ErrorResponse> ResponseEntity<T> handleLogicViolationException(LogicViolationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse(ex.getStatus(), ex.getMessage(), ex.getErrors());
        return new ResponseEntity<T>((T) response, ex.getHttpStatus());
    }
}
