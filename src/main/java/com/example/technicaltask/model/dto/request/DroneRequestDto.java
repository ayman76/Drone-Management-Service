package com.example.technicaltask.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneRequestDto {
    @NotNull
    private int model;

    @NotNull
    @Min(value = 0, message = "Battery Capacity Should be Positive value")
    @Max(value = 100, message = "Battery Capacity can not exceed 100%")
    private int batteryCapacity;
}
