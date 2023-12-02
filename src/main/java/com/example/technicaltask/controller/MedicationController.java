package com.example.technicaltask.controller;

import com.example.technicaltask.model.dto.request.MedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.example.technicaltask.utils.HelperFunctions.checkBindingResultErrorsAndReturn;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PostMapping("/create")
    public ResponseEntity<?> createMedication(@ModelAttribute @Valid MedicationRequestDto medicationRequest, BindingResult bindingResult) throws IOException {
        try {
            // Check if the image file is not null and its size exceeds the limit of 5MB
            if (medicationRequest.getImageFile() != null && medicationRequest.getImageFile().getSize() > 5 * 1024 * 1024) {
                FieldError fieldError = new FieldError("image", "image", "File size exceeds the limit of 5MB");
                bindingResult.addError(fieldError);
            }

            ResponseEntity<StringBuilder> errors = checkBindingResultErrorsAndReturn(bindingResult);

            if (errors != null) return errors;

            return new ResponseEntity<>(medicationService.createMedication(medicationRequest), HttpStatus.CREATED);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create medication with image");
        }

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<MedicationResponseDto>> getAllMedication(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(medicationService.getAllMedications(pageNo, pageSize), HttpStatus.OK);
    }
}
