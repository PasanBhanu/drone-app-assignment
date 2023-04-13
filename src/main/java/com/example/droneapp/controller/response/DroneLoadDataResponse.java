package com.example.droneapp.controller.response;

import com.example.droneapp.controller.model.MedicationData;
import lombok.Data;

import java.util.List;

@Data
public class DroneLoadDataResponse extends CommonResponse {
    private String serialNumber;
    private String droneState;
    private List<MedicationData> medicationData;
}
