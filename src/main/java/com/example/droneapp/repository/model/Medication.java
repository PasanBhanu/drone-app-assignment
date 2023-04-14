package com.example.droneapp.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "MEDICATION")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "DRONE_SERIAL_NUMBER")
    private String droneSerialNumber;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "WEIGHT")
    private Double weight;

    @Basic
    @Column(name = "CODE")
    private String code;

    @Lob
    @Column(name = "IMAGE")
    private String image;

    @Basic
    @Column(name = "STATUS")
    private Integer status;
}
