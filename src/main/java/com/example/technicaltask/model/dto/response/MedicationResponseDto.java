package com.example.technicaltask.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponseDto {

    private String code;
    private String name;
    private int weight;
    private String imageUUID;
}
