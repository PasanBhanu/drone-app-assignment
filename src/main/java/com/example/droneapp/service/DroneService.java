package com.example.droneapp.service;

import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.util.DroneState;
import org.springframework.stereotype.Service;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public CommonResponse registerDrone(RegisterDroneRequest request) {


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
