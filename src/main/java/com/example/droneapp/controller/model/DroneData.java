package com.example.droneapp.controller.model;

import com.example.droneapp.repository.model.Drone;

public class DroneData {
    private String serialNumber;
    private String model;
    private Double weightLimit;
    private Double battery;
    private String state;

    public DroneData(Drone dto) {
        this.serialNumber = dto.getSerialNumber();
        this.model = dto.getModel();
        this.weightLimit = dto.getWeightLimit();
        this.battery = dto.getBattery();
        this.state = dto.getState();
    }
}
