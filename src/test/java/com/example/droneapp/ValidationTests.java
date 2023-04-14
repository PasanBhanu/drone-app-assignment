package com.example.droneapp;

import com.example.droneapp.controller.request.LoadDroneRequest;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.exception.DatabaseValidationException;
import com.example.droneapp.exception.LogicViolationException;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.MedicationRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.service.DroneService;
import com.example.droneapp.util.DroneState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private DroneService droneService;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeEach
    void cleanDatabase() {
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test()
    void registerDroneValidationTest() throws Exception {
        RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
        registerDroneRequest.setModel("Lightweight");
        registerDroneRequest.setBattery(80.0);
        registerDroneRequest.setWeightLimit(100.0);

        mockMvc.perform(post("/drone/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setSerialNumber("");
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setModel("");
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setModel("ABCD");
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setBattery(-1.0);
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setBattery(150.0);
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setWeightLimit(-1.0);
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());

        registerDroneRequest.setWeightLimit(1000.0);
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDroneRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test()
    void loadDroneValidationTest() throws Exception {
        LoadDroneRequest loadDroneRequest = new LoadDroneRequest();
        loadDroneRequest.setName("N@ame");
        loadDroneRequest.setCode("ABCD");
        loadDroneRequest.setWeight(100.0);
        loadDroneRequest.setImage("IMAGE");

        mockMvc.perform(post("/drone/load/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loadDroneRequest)))
                .andExpect(status().isBadRequest());

        loadDroneRequest.setName("OK Name");
        loadDroneRequest.setCode("invalid code");
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loadDroneRequest)))
                .andExpect(status().isBadRequest());

        loadDroneRequest.setCode("ABCD");
        loadDroneRequest.setWeight(-1.0);
        mockMvc.perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loadDroneRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test()
    void registerDroneServiceValidationTest() {
        RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
        registerDroneRequest.setSerialNumber("TEST1");
        registerDroneRequest.setModel("Lightweight");
        registerDroneRequest.setBattery(80.0);
        registerDroneRequest.setWeightLimit(100.0);
        droneService.registerDrone(registerDroneRequest);
        assertThrows(DatabaseValidationException.class, () -> droneService.registerDrone(registerDroneRequest));
    }

    @Test()
    void loadDroneServiceValidationTest() {
        RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
        registerDroneRequest.setSerialNumber("TEST1");
        registerDroneRequest.setModel("Lightweight");
        registerDroneRequest.setBattery(80.0);
        registerDroneRequest.setWeightLimit(100.0);
        droneService.registerDrone(registerDroneRequest);

        LoadDroneRequest loadDroneRequest = new LoadDroneRequest();
        loadDroneRequest.setCode("ABCD");
        loadDroneRequest.setImage("data:image/png;base64,iVBOR=");
        loadDroneRequest.setWeight(10.0);
        loadDroneRequest.setName("Test-Medication");
        assertThrows(DatabaseValidationException.class, () -> droneService.loadDrone("TEST2", loadDroneRequest));
        assertThrows(LogicViolationException.class, () -> droneService.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest));

        loadDroneRequest.setWeight(200.0);
        assertThrows(LogicViolationException.class, () -> droneService.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest));

        Drone drone = droneRepository.findBySerialNumberIgnoreCase(registerDroneRequest.getSerialNumber()).get();
        drone.setState(DroneState.LOADING.name());
        droneRepository.save(drone);

        loadDroneRequest.setWeight(10.0);
        droneService.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest);

        loadDroneRequest.setWeight(100.0);
        assertThrows(LogicViolationException.class, () -> droneService.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest));
    }

    @Test()
    void getLoadingDroneServiceValidationTest() {
        assertThrows(DatabaseValidationException.class, () -> droneService.getLoadingDataForDrone("NOTVALID"));
    }

    @Test()
    void getBatteryDroneServiceValidationTest() {
        assertThrows(DatabaseValidationException.class, () -> droneService.checkDroneBattery("NOTVALID"));
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
