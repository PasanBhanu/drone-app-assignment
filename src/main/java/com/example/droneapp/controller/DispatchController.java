package com.example.droneapp.controller;

import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drone")
public class DispatchController {

    private final DroneService droneService;

    public DispatchController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> registerDrone(@RequestBody @Valid RegisterDroneRequest request) {
        return ResponseEntity.ok(droneService.registerDrone(request));
    }

    @PostMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse loadDrone(@RequestBody RegisterDroneRequest request) {
        return null;
    }

    @GetMapping(value = "/load/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLoadingDataForDrone(@PathVariable("serialNumber") String serialNumber) {
        return null;
    }

    @GetMapping(value = "/get-available-drones", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAvailableDrone(@RequestBody RegisterDroneRequest request) {
        return null;
    }

    @GetMapping(value = "/check-battery/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse checkDroneBattery(@PathVariable("serialNumber") String serialNumber) {
        return null;
    }
}
