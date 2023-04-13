package com.example.droneapp.controller;

import com.example.droneapp.controller.request.LoadDroneRequest;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.AvailableDroneResponse;
import com.example.droneapp.controller.response.BatteryStatusResponse;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.controller.response.DroneLoadDataResponse;
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

    @PostMapping(value = "/load/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> loadDrone(@PathVariable("serialNumber") String serialNumber, @RequestBody LoadDroneRequest request) {
        return ResponseEntity.ok(droneService.loadDrone(serialNumber, request));
    }

    @GetMapping(value = "/load/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DroneLoadDataResponse> getLoadingDataForDrone(@PathVariable("serialNumber") String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadingDataForDrone(serialNumber));
    }

    @GetMapping(value = "/get-available-drones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AvailableDroneResponse> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    @GetMapping(value = "/check-battery/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatteryStatusResponse> checkDroneBattery(@PathVariable("serialNumber") String serialNumber) {
        return ResponseEntity.ok(droneService.checkDroneBattery(serialNumber));
    }
}
