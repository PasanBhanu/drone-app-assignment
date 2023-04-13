package com.example.droneapp.service;

import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.exception.DatabaseValidationException;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.util.DroneState;
import com.example.droneapp.util.ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public CommonResponse registerDrone(RegisterDroneRequest request) {

        if (droneRepository.existsBySerialNumberIgnoreCase(request.getSerialNumber())) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_ALREADY_EXISTS, Collections.singletonList(new ValidationError("serial number", "record with same serial number found")));
        }

        Drone drone = new Drone();
        drone.setSerialNumber(request.getSerialNumber());
        drone.setModel(request.getModel());
        drone.setWeightLimit(request.getWeightLimit());
        drone.setState(DroneState.IDLE.name());
        droneRepository.save(drone);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccessResponse("drone added");
        return commonResponse;
    }
}
