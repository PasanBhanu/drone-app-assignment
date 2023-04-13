package com.example.droneapp.service;

import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.controller.request.LoadDroneRequest;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.exception.DatabaseValidationException;
import com.example.droneapp.exception.LogicViolationException;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.MedicationRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.repository.model.Medication;
import com.example.droneapp.util.DroneState;
import com.example.droneapp.util.ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    public CommonResponse registerDrone(RegisterDroneRequest request) {

        if (droneRepository.existsBySerialNumberIgnoreCase(request.getSerialNumber())) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_ALREADY_EXISTS, Collections.singletonList(new ValidationError("serial number", "record with same serial number found")));
        }

        Drone drone = new Drone();
        drone.setSerialNumber(request.getSerialNumber());
        drone.setModel(request.getModel());
        drone.setWeightLimit(request.getWeightLimit());
        drone.setBattery(request.getBattery());
        drone.setState(DroneState.IDLE.name());
        droneRepository.save(drone);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccessResponse("drone added");
        return commonResponse;
    }

    public CommonResponse loadDrone(String serialNumber, LoadDroneRequest request) {
        Optional<Drone> drone = droneRepository.findById(serialNumber);
        if (drone.isEmpty()) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_NOT_FOUND, Collections.singletonList(new ValidationError("serial number", "drone not found for this serial number")));
        }

        if (!drone.get().getState().equals(DroneState.IDLE.name())) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone is not in IDLE state")));
        }

        if (drone.get().getBattery() < 25) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone battery is below 25%")));
        }

        if (drone.get().getWeightLimit() < request.getWeight()) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone is unable to carry medication weight")));
        }

        Medication medication = new Medication();
        medication.setDroneSerialNumber(serialNumber);
        medication.setName(request.getName());
        medication.setWeight(request.getWeight());
        medication.setCode(request.getCode());
        medication.setImage(request.getImage());
        medicationRepository.save(medication);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccessResponse("medication added");
        return commonResponse;
    }
}
