package com.example.droneapp.repository;

import com.example.droneapp.repository.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    List<Medication> findByDroneSerialNumberIgnoreCase(String droneSerialNumber);

}
