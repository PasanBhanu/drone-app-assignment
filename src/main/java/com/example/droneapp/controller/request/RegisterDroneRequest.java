package com.example.droneapp.controller.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDroneRequest {
    @NotNull(message = "serial number is required")
    @NotBlank(message = "serial number is required")
    @Size(max = 100)
    private String serialNumber;
    @NotBlank(message = "model is required")
    @Pattern(regexp = "^(Lightweight|Middleweight|Cruiserweight|Heavyweight)$", message = "invalid model")
    private String model;
    @NotNull(message = "weight is required")
    @Max(value = 500, message = "maximum weight limit is 500g")
    @Min(value = 0, message = "weight should be more than zero")
    private Double weightLimit;
    @NotNull(message = "battery percentage is required")
    @Max(value = 100, message = "battery percentage should be between 0 - 100")
    @Min(value = 0, message = "battery percentage should be between 0 - 100")
    private Double battery;
}
