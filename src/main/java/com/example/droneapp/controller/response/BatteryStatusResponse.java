package com.example.droneapp.controller.response;

import lombok.Data;

@Data
public class BatteryStatusResponse extends CommonResponse {
    private String serialNumber;
    private Double battery;
}
