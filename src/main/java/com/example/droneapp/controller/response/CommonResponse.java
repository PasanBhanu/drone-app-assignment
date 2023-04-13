package com.example.droneapp.controller.response;

import lombok.Data;

@Data
public class CommonResponse {
    private Integer status = 501;
    private String message = "";

    public void setSuccessResponse(String description) {
        this.status = 200;
        this.message = description;
    }
}
