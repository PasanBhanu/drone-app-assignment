package com.example.droneapp.exception;

import com.example.droneapp.controller.model.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    private Integer status = 500;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String message = "Internal server error";
    private List<ValidationError> errors = null;
}
