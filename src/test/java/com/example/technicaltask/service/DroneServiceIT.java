package com.example.technicaltask.service;

import com.example.technicaltask.model.Drone;
import com.example.technicaltask.model.State;
import com.example.technicaltask.repository.DroneRepository;
import com.example.technicaltask.service.impl.DroneServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DroneServiceIT {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneServiceImpl droneService;

    @Test
    public void DroneService_CheckDroneBatteryCapacity() throws IOException {

        //Given
        List<Drone> drones = droneRepository.findAll().stream().sorted(Comparator.comparing(Drone::getState)).sorted(Comparator.comparing(Drone::getBatteryCapacity)).toList();

        //When
        droneService.checkDroneBatteryLevel();

        // Define the path to the log file
        String logFilePath = "logs/center-management-logs/Log.log";

        // Check if the log file exists
        Path path = Paths.get(logFilePath);
        assertTrue(Files.exists(path), "Log file does not exist at: " + logFilePath);

        String fileContent = Files.readString(path);

        //Then
        assertTrue(fileContent.contains("Check Drone Battery Capacity"));
        assertTrue(fileContent.contains("Available Drones to load: " + drones.stream().filter(drone -> drone.getState() == State.IDLE && drone.getBatteryCapacity() > 25).count()));
        assertTrue(fileContent.contains("Serial Number                                Battery Capacity            State"));
        assertTrue(fileContent.contains("Drone " + drones.get(0).getSerialNumber() + ":        " + drones.get(0).getBatteryCapacity() + "                    " + drones.get(0).getState()));

    }
}
