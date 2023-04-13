package com.example.droneapp.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse extends CommonResponse {
    public ErrorResponse(Integer status, String message) {
        this.setStatus(status);
        this.setMessage(message);
    }
}
