package com.example.technicaltask.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadedMedicationRequestDto {
    @NotNull(message = "Medication Code could not be null")
    @NotBlank(message = "Medication Code could not be empty")
    private String code;
}
