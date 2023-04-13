package com.example.droneapp.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DRONE")
public class Drone {
    @Id
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Basic
    @Column(name = "MODEL")
    private String model;

    @Basic
    @Column(name = "WEIGHT_LIMIT")
    private Double weightLimit;

    @Basic
    @Column(name = "BATTERY")
    private Double battery;

    @Basic
    @Column(name = "STATE")
    private String state;
}
