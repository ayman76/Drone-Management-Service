package com.example.technicaltask.service.impl;

import com.example.technicaltask.model.Medication;
import com.example.technicaltask.model.dto.request.MedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.repository.MedicationRepository;
import com.example.technicaltask.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.technicaltask.utils.HelperFunctions.getApiResponse;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new Medication based on the provided MedicationRequestDto.
     * Generates a unique code for the medication and saves it to the repository.
     * If an image file is provided, stores the image data and generates an image UUID.
     */
    @Override
    public MedicationResponseDto createMedication(MedicationRequestDto medicationRequest) throws IOException {
        Medication medication = modelMapper.map(medicationRequest, Medication.class);
        medication.generateCode();
        if (medicationRequest.getImageFile() != null) {
            byte[] image = medicationRequest.getImageFile().getBytes();
            medication.setImage(image);
            medication.setImageUUID(generateImageUUID());
        }
        Medication savedMedication = medicationRepository.save(medication);
        return modelMapper.map(savedMedication, MedicationResponseDto.class);

    }

    /**
     * Retrieves a paginated list of all medications available in the repository.
     */
    @Override
    public ApiResponse<MedicationResponseDto> getAllMedications(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Medication> medications = medicationRepository.findAll(pageable);
        List<Medication> medicationList = medications.getContent();
        List<MedicationResponseDto> content = medicationList.stream().map(m -> modelMapper.map(m, MedicationResponseDto.class)).toList();

        return getApiResponse(pageNo, pageSize, content, medications);
    }

    /**
     * Generates a unique UUID string for the image associated with a Medication.
     */
    public String generateImageUUID() {
        return "IMG_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
    }
}
