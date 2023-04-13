package com.example.droneapp.exception;

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
    private List<String> errors = null;

    public LogicViolationException(Integer status, HttpStatus httpStatus, String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
