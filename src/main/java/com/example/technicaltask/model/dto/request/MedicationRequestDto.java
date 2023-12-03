package com.example.technicaltask.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp = "[a-zA-Z0-9-_]+", message = "Invalid name (Name should consists of letters, numbers, ‘-‘, ‘_’)")
    private String name;
    @NotNull(message = "Weight must not be null")
    @Min(value = 0, message = "Weight must be positive value")
    private Integer weight;

    private MultipartFile imageFile;
}
