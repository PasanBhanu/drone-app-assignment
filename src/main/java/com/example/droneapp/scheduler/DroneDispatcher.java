package com.example.droneapp.scheduler;

import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.MedicationRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.repository.model.Medication;
import com.example.droneapp.util.DroneState;
import com.example.droneapp.util.MedicationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
@Component
public class DroneDispatcher {

    private final MedicationRepository medicationRepository;
    private final DroneRepository droneRepository;

    public DroneDispatcher(MedicationRepository medicationRepository, DroneRepository droneRepository) {
        this.medicationRepository = medicationRepository;
        this.droneRepository = droneRepository;
    }

    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
        log.info("Drone Dispatcher Running");

        // Randomly Perform Action
        Random random = new Random();
        int execution = random.nextInt(6);
        log.info("Random no generated {}", execution);

        switch (execution){
            case 0:
                this.startLoading();
                break;
            case 1:
                this.completeLoading();
                break;
            case 2:
                this.dispatchDrones();
                break;
            case 3:
                this.deliveryConfirmation();
                break;
            case 4:
                this.returnDrones();
                break;
            case 5:
                this.returnToWarehouse();
                break;
        }
    }

    private void startLoading() {
        log.info("Executing drone load");
        List<Medication> medicationList = medicationRepository.findByStatus(MedicationStatus.ACTIVE.getValue());
        Set<Drone> droneList = new HashSet<>();
        medicationList.stream().forEach(m -> {
            droneList.add(droneRepository.findBySerialNumberIgnoreCase(m.getDroneSerialNumber()).get());
        });

        medicationList.stream().forEach(m -> {
            m.setStatus(MedicationStatus.DISPATCHED.getValue());
        });
        medicationRepository.saveAll(medicationList);

        droneList.stream().forEach(d -> {
            log.info("Action for Drone {} - {}", d.getSerialNumber(), DroneState.LOADING);
            d.setState(DroneState.LOADING.name());
        });
        droneRepository.saveAll(droneList);
    }

    private void completeLoading() {
        log.info("Executing drone load complete");
        List<Drone> droneList = droneRepository.findByState(DroneState.LOADING.name());
        droneList.stream().forEach(d -> {
            log.info("Action for Drone {} - {}", d.getSerialNumber(), DroneState.LOADED);
            d.setState(DroneState.LOADED.name());
        });
        droneRepository.saveAll(droneList);
    }

    private void dispatchDrones() {
        log.info("Executing drone dispatch");
        List<Drone> droneList = droneRepository.findByState(DroneState.LOADED.name());
        droneList.stream().forEach(d -> {
            log.info("Action for Drone {} - {}", d.getSerialNumber(), DroneState.DELIVERING);
            d.setState(DroneState.DELIVERING.name());
        });
        droneRepository.saveAll(droneList);
    }

    private void deliveryConfirmation() {
        log.info("Executing delivery confirmation");
        List<Drone> droneList = droneRepository.findByState(DroneState.DELIVERING.name());
        droneList.stream().forEach(d -> {
            log.info("Action for Drone {} - {}", d.getSerialNumber(), DroneState.DELIVERED);
            d.setState(DroneState.DELIVERED.name());
        });
        droneRepository.saveAll(droneList);
    }

    private void returnDrones() {
        log.info("Executing drone return");
        List<Drone> droneList = droneRepository.findByState(DroneState.DELIVERED.name());
        droneList.stream().forEach(d -> {
            log.info("Action for Drone {} - {}", d.getSerialNumber(), DroneState.RETURNING);
            d.setState(DroneState.RETURNING.name());
        });
        droneRepository.saveAll(droneList);
    }

    private void returnToWarehouse() {
        log.info("Executing drone idle");
        List<Drone> droneList = droneRepository.findByState(DroneState.RETURNING.name());
        droneList.stream().forEach(d -> {
            // Get Randoms (1-20%) for Battery Reduction
            Random random = new Random();
            int batteryForRide = random.nextInt(20) + 1;
            d.setBattery(d.getBattery() - batteryForRide);
            log.info("Action for Drone {} - {} | Battery after {}", d.getSerialNumber(), DroneState.IDLE, d.getBattery());
            d.setState(DroneState.IDLE.name());
        });
        droneRepository.saveAll(droneList);
    }
}
