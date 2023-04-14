package com.example.droneapp.repository;

import com.example.droneapp.repository.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    List<Medication> findByDroneSerialNumberIgnoreCaseAndStatus(String droneSerialNumber, Integer status);

    List<Medication> findByStatus(Integer status);

    List<Medication> findByDroneSerialNumberInIgnoreCaseAndStatus(Collection<String> droneSerialNumbers, Integer status);

    @Query("SELECT SUM(m.weight) FROM Medication m WHERE LOWER(m.droneSerialNumber) = LOWER(:serialNumber) AND m.status = 1")
    Double totalWeightOfDrone(@Param("serialNumber") String serialNumber);
}
