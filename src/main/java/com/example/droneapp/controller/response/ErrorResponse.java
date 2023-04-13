package com.example.droneapp.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse extends CommonResponse {
    private List<String> errors = null;

    public ErrorResponse(Integer status, String message, List<String> errors) {
        this.setStatus(status);
        this.setMessage(message);
        this.errors = errors;
    }

    public ErrorResponse(Integer status, String message) {
        this.setStatus(status);
        this.setMessage(message);
        this.errors = errors;
    }
}
