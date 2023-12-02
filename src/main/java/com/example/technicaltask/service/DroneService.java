package com.example.technicaltask.service;

import com.example.technicaltask.model.dto.request.DroneRequestDto;
import com.example.technicaltask.model.dto.request.LoadedMedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.DroneResponseDto;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;

import java.util.List;

public interface DroneService {
    DroneResponseDto registerDrone(DroneRequestDto drone);

    ApiResponse<DroneResponseDto> getAllAvailableDrones(int pageNo, int pageSize);

    int getBatteryLevelForDrone(String serialNumber);

    DroneResponseDto loadMedicationsToDrone(String droneSerialNumber, List<LoadedMedicationRequestDto> medication);

    ApiResponse<MedicationResponseDto> getLoadedMedications(String droneSerialNumber, int pageNo, int pageSize);
}