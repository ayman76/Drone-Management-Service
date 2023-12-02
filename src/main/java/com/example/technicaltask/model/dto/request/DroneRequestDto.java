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
    @NotNull(message = "Model should not be empty")
    @Min(value = 0, message = "Model not founded")
    @Max(value = 3, message = "Model not founded")
    private Integer model;

    @NotNull(message = "Battery Capacity should not be empty")
    @Min(value = 0, message = "Battery Capacity Should be Positive value")
    @Max(value = 100, message = "Battery Capacity can not exceed 100%")
    private Integer batteryCapacity;
}
