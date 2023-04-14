package com.example.droneapp;

import com.example.droneapp.controller.DispatchController;
import com.example.droneapp.controller.request.LoadDroneRequest;
import com.example.droneapp.controller.request.RegisterDroneRequest;
import com.example.droneapp.controller.response.AvailableDroneResponse;
import com.example.droneapp.controller.response.BatteryStatusResponse;
import com.example.droneapp.controller.response.CommonResponse;
import com.example.droneapp.controller.response.DroneLoadDataResponse;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.repository.MedicationRepository;
import com.example.droneapp.repository.model.Drone;
import com.example.droneapp.repository.model.Medication;
import com.example.droneapp.util.DroneState;
import com.example.droneapp.util.MedicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DroneappApplicationTests {

	@Autowired
	private DispatchController dispatchController;
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

	@Test
	void registerDroneTest() {
		RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
		registerDroneRequest.setSerialNumber("TEST1");
		registerDroneRequest.setModel("Lightweight");
		registerDroneRequest.setBattery(80.0);
		registerDroneRequest.setWeightLimit(100.0);
		ResponseEntity<CommonResponse> response = dispatchController.registerDrone(registerDroneRequest);

		assertThat(response.getBody().getStatus()).isEqualTo(200);
	}

	@Test
	void loadDroneTest() {
		RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
		registerDroneRequest.setSerialNumber("TEST2");
		registerDroneRequest.setModel("Lightweight");
		registerDroneRequest.setBattery(80.0);
		registerDroneRequest.setWeightLimit(100.0);
		dispatchController.registerDrone(registerDroneRequest);

		Drone drone = droneRepository.findBySerialNumberIgnoreCase(registerDroneRequest.getSerialNumber()).get();
		drone.setState(DroneState.LOADING.name());
		droneRepository.save(drone);

		LoadDroneRequest loadDroneRequest = new LoadDroneRequest();
		loadDroneRequest.setCode("ABCD");
		loadDroneRequest.setImage("data:image/png;base64,iVBOR=");
		loadDroneRequest.setWeight(10.0);
		loadDroneRequest.setName("Test-Medication");
		ResponseEntity<CommonResponse> response = dispatchController.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest);

		assertThat(response.getBody().getStatus()).isEqualTo(200);
	}

	@Test
	void getDroneLoadingTest() {
		RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
		registerDroneRequest.setSerialNumber("TEST3");
		registerDroneRequest.setModel("Lightweight");
		registerDroneRequest.setBattery(80.0);
		registerDroneRequest.setWeightLimit(100.0);
		dispatchController.registerDrone(registerDroneRequest);

		ResponseEntity<DroneLoadDataResponse> response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(0);

		Drone drone = droneRepository.findBySerialNumberIgnoreCase(registerDroneRequest.getSerialNumber()).get();
		drone.setState(DroneState.LOADING.name());
		droneRepository.save(drone);

		LoadDroneRequest loadDroneRequest = new LoadDroneRequest();
		loadDroneRequest.setCode("ABCD");
		loadDroneRequest.setImage("data:image/png;base64,iVBOR=");
		loadDroneRequest.setWeight(10.0);
		loadDroneRequest.setName("Test-Medication");
		dispatchController.loadDrone(registerDroneRequest.getSerialNumber(), loadDroneRequest);

		List<Medication> medicationList = medicationRepository.findByDroneSerialNumberIgnoreCaseAndStatus(registerDroneRequest.getSerialNumber(), MedicationStatus.ACTIVE.getValue());
		assertThat(medicationList.size()).isEqualTo(1);
		Medication medication = medicationList.get(0);

		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(1);

		drone.setState(DroneState.LOADED.name());
		droneRepository.save(drone);
		medication.setStatus(MedicationStatus.DISPATCHED.getValue());
		medicationRepository.save(medication);

		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(1);

		drone.setState(DroneState.DELIVERING.name());
		droneRepository.save(drone);
		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(1);

		drone.setState(DroneState.DELIVERED.name());
		droneRepository.save(drone);
		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(1);

		drone.setState(DroneState.RETURNING.name());
		droneRepository.save(drone);
		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(1);

		drone.setState(DroneState.IDLE.name());
		droneRepository.save(drone);
		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(0);

		drone.setState(DroneState.LOADING.name());
		droneRepository.save(drone);
		response = dispatchController.getLoadingDataForDrone(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getMedicationData().size()).isEqualTo(0);
	}

	@Test
	void availableDroneTest() {
		RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
		registerDroneRequest.setSerialNumber("TEST4");
		registerDroneRequest.setModel("Lightweight");
		registerDroneRequest.setBattery(80.0);
		registerDroneRequest.setWeightLimit(100.0);
		dispatchController.registerDrone(registerDroneRequest);

		Drone drone = droneRepository.findBySerialNumberIgnoreCase(registerDroneRequest.getSerialNumber()).get();
		drone.setState(DroneState.LOADING.name());
		droneRepository.save(drone);

		ResponseEntity<AvailableDroneResponse> response = dispatchController.getAvailableDrones();
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getDrones().size()).isEqualTo(1);

		drone.setState(DroneState.IDLE.name());
		droneRepository.save(drone);

		response = dispatchController.getAvailableDrones();
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getDrones().size()).isEqualTo(0);
	}

	@Test
	void droneBatteryTest() {
		RegisterDroneRequest registerDroneRequest = new RegisterDroneRequest();
		registerDroneRequest.setSerialNumber("TEST5");
		registerDroneRequest.setModel("Lightweight");
		registerDroneRequest.setBattery(80.0);
		registerDroneRequest.setWeightLimit(100.0);
		dispatchController.registerDrone(registerDroneRequest);

		ResponseEntity<BatteryStatusResponse> response = dispatchController.checkDroneBattery(registerDroneRequest.getSerialNumber());
		assertThat(response.getBody().getStatus()).isEqualTo(200);
		assertThat(response.getBody().getBattery()).isEqualTo(80.0);
	}
}
