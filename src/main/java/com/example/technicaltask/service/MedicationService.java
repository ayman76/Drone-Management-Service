package com.example.technicaltask.service;

import com.example.technicaltask.model.dto.request.MedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;

import java.io.IOException;

public interface MedicationService {

    MedicationResponseDto createMedication(MedicationRequestDto medicationRequest) throws IOException;

    ApiResponse<MedicationResponseDto> getAllMedications(int pageNo, int pageSize);

}
