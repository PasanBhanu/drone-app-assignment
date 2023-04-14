package com.example.droneapp.util;

public enum MedicationStatus {
    ACTIVE(1),
    DISPATCHED(2),
    COMPLETED(3);

    private final Integer value;

    MedicationStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
