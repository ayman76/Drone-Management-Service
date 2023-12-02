package com.example.technicaltask.service.impl;

import com.example.technicaltask.exception.NotAvailableDroneException;
import com.example.technicaltask.exception.ResourceNotFoundException;
import com.example.technicaltask.exception.WeightLimitExceededException;
import com.example.technicaltask.model.Drone;
import com.example.technicaltask.model.Medication;
import com.example.technicaltask.model.State;
import com.example.technicaltask.model.dto.request.DroneRequestDto;
import com.example.technicaltask.model.dto.request.LoadedMedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.DroneResponseDto;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.repository.DroneRepository;
import com.example.technicaltask.repository.MedicationRepository;
import com.example.technicaltask.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.technicaltask.utils.HelperFunctions.getApiResponse;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final ModelMapper modelMapper;

    /**
     * Registers a new drone based on the provided DroneRequestDto.
     */
    @Override
    public DroneResponseDto registerDrone(DroneRequestDto drone) {
        Drone drone1 = modelMapper.map(drone, Drone.class);
        drone1.setWeightLimit(drone1.getModel().getValue());
        Drone savedDrone = droneRepository.save(drone1);
        return modelMapper.map(savedDrone, DroneResponseDto.class);
    }

    /**
     * Retrieves a paginated list of all available drones based on the provided page number and page size.
     */
    @Override
    public ApiResponse<DroneResponseDto> getAllAvailableDrones(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Drone> drones = droneRepository.findAllAvailableDrones(pageable, State.IDLE);
        List<Drone> droneList = drones.getContent();
        List<DroneResponseDto> content = droneList.stream().map(d -> modelMapper.map(d, DroneResponseDto.class)).toList();

        return getApiResponse(pageNo, pageSize, content, drones);
    }

    /**
     * Retrieves the battery level (capacity) for the specified drone based on its serial number.
     */
    @Override
    public int getBatteryLevelForDrone(String serialNumber) {
        Drone foundedDrone = findDroneBySerialNumber(serialNumber);
        return foundedDrone.getBatteryCapacity();
    }

    /**
     * Loads medications onto a drone based on the provided serial number and list of medication requests.
     */
    @Override
    public DroneResponseDto loadMedicationsToDrone(String droneSerialNumber, List<LoadedMedicationRequestDto> medication) {
        Drone foundedDrone = findDroneBySerialNumber(droneSerialNumber);
        // Check if the drone is available for loading medications based on its state or its battery capacity
        if (foundedDrone.getState() != State.IDLE || foundedDrone.getBatteryCapacity() < 25) {
            throw new NotAvailableDroneException("Drone Not Available to load medication on it");
        }

        // Retrieve the list of medications to load based on the provided request DTOs
        List<Medication> toLoadMedication = medication.stream().map(m -> medicationRepository.findById(m.getCode()).orElseThrow(() -> new ResourceNotFoundException("Not Founded Medication with code: " + m.getCode()))).toList();

        // Check if the total weight of medications exceeds the drone's weight limit
        if (toLoadMedication.stream().mapToInt(Medication::getWeight).sum() > foundedDrone.getWeightLimit()) {
            throw new WeightLimitExceededException("Weight Limit should not be greater than " + foundedDrone.getWeightLimit() + "gm for Drone Model " + foundedDrone.getModel());
        }

        foundedDrone.setState(State.LOADING);
        droneRepository.save(foundedDrone);

        // Create a new list to hold the loaded medications and add all medications to it
        List<Medication> loadedMedications = new ArrayList<>(foundedDrone.getLoadedMedications());
        loadedMedications.addAll(toLoadMedication);

        foundedDrone.setLoadedMedications(loadedMedications);
        foundedDrone.setState(State.LOADED);
        Drone updatedDrone = droneRepository.save(foundedDrone);
        return modelMapper.map(updatedDrone, DroneResponseDto.class);

    }

    /**
     * Retrieves a paginated list of loaded medications for a drone based on the provided serial number.
     */
    @Override
    public ApiResponse<MedicationResponseDto> getLoadedMedications(String droneSerialNumber, int pageNo, int pageSize) {
        Drone foundedDrone = findDroneBySerialNumber(droneSerialNumber);

        List<MedicationResponseDto> content = foundedDrone.getLoadedMedications().stream().map(m -> modelMapper.map(m, MedicationResponseDto.class)).toList();

        // Create a PageImpl from the content to simulate pagination
        Page<MedicationResponseDto> medications = new PageImpl<>(content);

        return getApiResponse(pageNo, pageSize, content, medications);

    }

    public Drone findDroneBySerialNumber(String serialNumber) {
        return droneRepository.findById(serialNumber).orElseThrow(() -> new ResourceNotFoundException("Not founded drone with serial number: " + serialNumber));
    }

}