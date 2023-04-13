package com.example.droneapp.controller.request;

import lombok.Data;

@Data
public class LoadDroneRequest {
    private String name;
    private Double weight;
    private String code;
    private String image;
}
