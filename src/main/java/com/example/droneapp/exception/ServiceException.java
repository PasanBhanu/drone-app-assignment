package com.example.droneapp.exception;

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
    private List<String> errors = null;

    public ServiceException(Integer status, HttpStatus httpStatus, String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
