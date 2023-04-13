package com.example.droneapp.controller.response;

import com.example.droneapp.controller.model.DroneData;
import lombok.Data;

import java.util.List;

@Data
public class AvailableDroneResponse extends CommonResponse{
    private List<DroneData> drones;
}
