package com.example.droneapp.repository;

import com.example.droneapp.repository.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, String> {

}
