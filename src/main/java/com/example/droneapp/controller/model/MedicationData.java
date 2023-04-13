package com.example.droneapp.controller.model;

import com.example.droneapp.repository.model.Medication;
import lombok.Data;

@Data
public class MedicationData {
    private Integer medicationId;
    private String name;
    private Double weight;
    private String code;
    private String image;

    public MedicationData(Medication dto) {
        this.medicationId = dto.getId();
        this.name = dto.getName();
        this.weight = dto.getWeight();
        this.code = dto.getCode();
        this.image = dto.getImage();
    }
}
