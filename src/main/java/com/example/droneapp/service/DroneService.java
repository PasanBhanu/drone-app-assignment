package com.example.droneapp.service;

import com.example.droneapp.controller.model.DroneData;
import com.example.droneapp.controller.model.MedicationData;
import com.example.droneapp.controller.model.ValidationError;
import com.example.droneapp.controller.request.LoadDroneRequest;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.AvailableDroneResponse;
import com.example.droneapp.controller.response.BatteryStatusResponse;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.controller.response.DroneLoadDataResponse;
import com.example.droneapp.exception.DatabaseValidationException;
import com.example.droneapp.exception.LogicViolationException;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.MedicationRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.repository.model.Medication;
import com.example.droneapp.util.DroneState;
import com.example.droneapp.util.ErrorCodes;
import com.example.droneapp.util.MedicationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    /**
     * Register Drone
     * @param request Drone Data
     * @return Status
     */
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

        log.info("Drone added with Serial Number {} - {}", request.getSerialNumber(), DroneState.IDLE);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccessResponse("drone added");
        return commonResponse;
    }

    /**
     * Load Drone
     * @param serialNumber Drone Serial Number
     * @param request Load Data
     * @return Status
     */
    public CommonResponse loadDrone(String serialNumber, LoadDroneRequest request) {
        Optional<Drone> drone = droneRepository.findBySerialNumberIgnoreCase(serialNumber);
        if (drone.isEmpty()) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_NOT_FOUND, Collections.singletonList(new ValidationError("serial number", "drone not found for this serial number")));
        }

        if (!drone.get().getState().equals(DroneState.LOADING.name())) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone is not in LOADING state")));
        }

        if (drone.get().getWeightLimit() < request.getWeight()) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone is unable to carry medication weight")));
        }

        Double currentWeight = Optional.ofNullable(medicationRepository.totalWeightOfDrone(serialNumber)).orElse(0.0);
        if (drone.get().getWeightLimit() < (currentWeight + request.getWeight())) {
            throw new LogicViolationException(ErrorCodes.INVALID_STATE, Collections.singletonList(new ValidationError("all", "drone weight is exceeding")));
        }

        Medication medication = new Medication();
        medication.setDroneSerialNumber(serialNumber);
        medication.setName(request.getName());
        medication.setWeight(request.getWeight());
        medication.setCode(request.getCode());
        medication.setImage(request.getImage());
        medication.setStatus(MedicationStatus.ACTIVE.getValue());
        medicationRepository.save(medication);

        log.info("Drone {} loaded. Weight: {} Battery: {}% Status: {}", serialNumber, currentWeight + request.getWeight(), drone.get().getBattery(), drone.get().getState());

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setSuccessResponse("medication added");
        return commonResponse;
    }

    /**
     * Get Loading Data for Drone
     * @param serialNumber Drone Serial Number
     * @return Loading Data
     */
    public DroneLoadDataResponse getLoadingDataForDrone(String serialNumber) {
        Optional<Drone> drone = droneRepository.findBySerialNumberIgnoreCase(serialNumber);
        if (drone.isEmpty()) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_NOT_FOUND, Collections.singletonList(new ValidationError("serial number", "drone not found for this serial number")));
        }

        log.info("Get load for Drone {} with status {}", serialNumber, drone.get().getState());

        /**
         * Get Loading for Drone
         *
         * LOADING -> ACTIVE
         * IDLE -> NONE
         * OTHERS -> DISPATCHED
         */
        List<Medication> medicationList = new ArrayList<>();
        if (drone.get().getState().equals(DroneState.LOADING.name())) {
            medicationList = medicationRepository.findByDroneSerialNumberIgnoreCaseAndStatus(serialNumber, MedicationStatus.ACTIVE.getValue());
        } else if (drone.get().getState().equals(DroneState.LOADED.name())
                || drone.get().getState().equals(DroneState.LOADING.name())
                || drone.get().getState().equals(DroneState.DELIVERING.name())
                || drone.get().getState().equals(DroneState.DELIVERED.name())
                || drone.get().getState().equals(DroneState.DELIVERED.name())
                || drone.get().getState().equals(DroneState.RETURNING.name())) {
            medicationList = medicationRepository.findByDroneSerialNumberIgnoreCaseAndStatus(serialNumber, MedicationStatus.DISPATCHED.getValue());
        }

        DroneLoadDataResponse response = new DroneLoadDataResponse();
        response.setSuccessResponse("data loaded");
        response.setSerialNumber(serialNumber);
        response.setDroneState(drone.get().getState());
        response.setMedicationData(medicationList.stream().map(MedicationData::new).collect(Collectors.toList()));
        return response;
    }

    /**
     * Get Available Drones for Loading
     * @return Available Drone List
     */
    public AvailableDroneResponse getAvailableDrones() {
        List<Drone> droneList = droneRepository.findByState(DroneState.LOADING.name());

        AvailableDroneResponse response = new AvailableDroneResponse();
        response.setSuccessResponse("data loaded");
        response.setDrones(droneList.stream().map(DroneData::new).collect(Collectors.toList()));
        return response;
    }

    /**
     * Check Drone Battery
     * @param serialNumber Drone Serial Number
     * @return Battery Percentage
     */
    public BatteryStatusResponse checkDroneBattery(String serialNumber) {
        Optional<Drone> drone = droneRepository.findBySerialNumberIgnoreCase(serialNumber);
        if (drone.isEmpty()) {
            throw new DatabaseValidationException(ErrorCodes.RECORD_NOT_FOUND, Collections.singletonList(new ValidationError("serial number", "drone not found for this serial number")));
        }

        BatteryStatusResponse response = new BatteryStatusResponse();
        response.setSuccessResponse("data loaded");
        response.setSerialNumber(serialNumber);
        response.setBattery(drone.get().getBattery());
        return response;
    }
}
