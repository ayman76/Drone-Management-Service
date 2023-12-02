package com.example.technicaltask.controller;

import com.example.technicaltask.model.dto.request.DroneRequestDto;
import com.example.technicaltask.model.dto.request.LoadedMedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.DroneResponseDto;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.service.impl.DroneServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.technicaltask.utils.HelperFunctions.checkBindingResultErrorsAndReturn;

@RestController
@RequestMapping("/api/v1/drone")
@RequiredArgsConstructor
public class DroneController {
    private final DroneServiceImpl droneService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDrone(@RequestBody @Valid DroneRequestDto drone, BindingResult bindingResult) {
        ResponseEntity<StringBuilder> errors = checkBindingResultErrorsAndReturn(bindingResult);
        if (errors != null) return errors;
        return new ResponseEntity<>(droneService.registerDrone(drone), HttpStatus.CREATED);
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<?> loadMedicationsOnDrone(@PathVariable String serialNumber, @RequestBody @Valid List<LoadedMedicationRequestDto> medications, BindingResult bindingResult) {
        ResponseEntity<StringBuilder> errors = checkBindingResultErrorsAndReturn(bindingResult);
        if (errors != null) return errors;
        return new ResponseEntity<>(droneService.loadMedicationsToDrone(serialNumber, medications), HttpStatus.OK);
    }

    @GetMapping("/available-for-loading")
    public ResponseEntity<ApiResponse<DroneResponseDto>> getAllAvailableDrones(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(droneService.getAllAvailableDrones(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{serialNumber}/battery-level")
    public ResponseEntity<Integer> getBatteryLevelForDrone(@PathVariable String serialNumber) {
        return new ResponseEntity<>(droneService.getBatteryLevelForDrone(serialNumber), HttpStatus.OK);
    }

    @GetMapping("/{serialNumber}/loaded-medications")
    public ResponseEntity<ApiResponse<MedicationResponseDto>> getLoadedMedications(@PathVariable String serialNumber, @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(droneService.getLoadedMedications(serialNumber, pageNo, pageSize), HttpStatus.OK);
    }
}