package com.example.technicaltask.model.dto.response;

import com.example.technicaltask.model.Model;
import com.example.technicaltask.model.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneResponseDto {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private State state;
    private List<MedicationResponseDto> medications;
}
