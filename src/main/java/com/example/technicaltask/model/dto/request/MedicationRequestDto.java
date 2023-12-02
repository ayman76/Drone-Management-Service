package com.example.technicaltask.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestDto {
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9-_]+", message = "Invalid name (Name should consists of letters, numbers, ‘-‘, ‘_’)")
    private String name;
    @NotNull
    @Min(value = 0, message = "Weight must be positive value")
    private int weight;

    private MultipartFile imageFile;
}
