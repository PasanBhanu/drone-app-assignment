package com.example.droneapp.controller.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoadDroneRequest {
    @NotBlank(message = "name is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "invalid name")
    private String name;
    @NotNull(message = "weight is required")
    @Min(value = 0, message = "weight should be more than zero")
    private Double weight;
    @NotBlank (message = "code is required")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "invalid code")
    private String code;
    @NotBlank(message = "image is required")
    private String image;
}
