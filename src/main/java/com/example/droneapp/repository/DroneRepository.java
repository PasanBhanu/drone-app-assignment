package com.example.droneapp.repository;

import com.example.droneapp.repository.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, String> {
    boolean existsBySerialNumberIgnoreCase(String serialNumber);

    Optional<Drone> findBySerialNumberIgnoreCase(String serialNumber);

    List<Drone> findByState(String state);
}
