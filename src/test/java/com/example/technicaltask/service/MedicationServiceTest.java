package com.example.technicaltask.service;

import com.example.technicaltask.model.Medication;
import com.example.technicaltask.model.dto.request.MedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.repository.MedicationRepository;
import com.example.technicaltask.service.impl.MedicationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private ModelMapper modelMapper;
    @Spy
    @InjectMocks
    private MedicationServiceImpl medicationService;

    @BeforeEach
    public void setup() {
        medicationService = new MedicationServiceImpl(medicationRepository, modelMapper);
    }

    @Test
    public void MedicationService_createMedicationWithImage_ReturnMedicationResponseDto() throws IOException {
        MultipartFile imageFile = Mockito.mock(MultipartFile.class);
        MedicationRequestDto mockMedicationRequest = MedicationRequestDto.builder().name("Med-1").weight(25).imageFile(imageFile).build();
        Medication mockMedication = Medication.builder().name(mockMedicationRequest.getName()).weight(mockMedicationRequest.getWeight()).image(mockMedicationRequest.getImageFile().getBytes()).imageUUID(medicationService.generateImageUUID()).build();
        mockMedication.generateCode();

        MedicationResponseDto mockMedicationResponse = MedicationResponseDto.builder().name(mockMedication.getName()).weight(mockMedication.getWeight()).code(mockMedication.getCode()).imageUUID(mockMedication.getImageUUID()).build();

        when(modelMapper.map(Mockito.any(MedicationRequestDto.class), Mockito.any())).thenReturn(mockMedication);
        when(imageFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(modelMapper.map(Mockito.any(Medication.class), Mockito.any())).thenReturn(mockMedicationResponse);
        when(medicationRepository.save(Mockito.any(Medication.class))).thenReturn(mockMedication);

        MedicationResponseDto result = medicationService.createMedication(mockMedicationRequest);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void MedicationService_createMedicationWithoutImage_ReturnMedicationResponseDto() throws IOException {
        //given
        MedicationRequestDto mockMedicationRequest = new MedicationRequestDto();
        mockMedicationRequest.setName("Med-1");
        mockMedicationRequest.setWeight(25);

        Medication mockMedication = Medication.builder().name("Med-1").weight(25).build();
        mockMedication.generateCode();

        MedicationResponseDto mockMedicationResponse = MedicationResponseDto.builder().name("Med-1").weight(25).code(mockMedication.getCode()).build();


        when(modelMapper.map(Mockito.any(MedicationRequestDto.class), Mockito.any())).thenReturn(mockMedication);
        when(modelMapper.map(Mockito.any(Medication.class), Mockito.any())).thenReturn(mockMedicationResponse);
        when(medicationRepository.save(Mockito.any(Medication.class))).thenReturn(mockMedication);

        MedicationResponseDto result = medicationService.createMedication(mockMedicationRequest);

        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void MedicationService_GetAllMedications1_ReturnMedicationResponseDtos() {
        Medication mockMedication = Medication.builder().name("Med-1").weight(25).build();
        mockMedication.generateCode();

        MedicationResponseDto mockMedicationResponse = MedicationResponseDto.builder().name("Med-1").weight(25).code(mockMedication.getCode()).build();

        Pageable pageable = PageRequest.of(0, 10);

        List<Medication> mockMedicationList = List.of(mockMedication);
        Page<Medication> mockPage = new PageImpl<>(mockMedicationList);

        // Mocking behavior of medicationRepository.findAll() method
        when(medicationRepository.findAll(pageable)).thenReturn(mockPage);

        // Mocking behavior of modelMapper.map()
        List<MedicationResponseDto> mockResponseList = List.of(mockMedicationResponse);
        when(modelMapper.map(mockMedicationList.get(0), MedicationResponseDto.class)).thenReturn(mockResponseList.get(0));

        // Calling the method to test
        ApiResponse<MedicationResponseDto> result = medicationService.getAllMedications(0, 10);

        Assertions.assertThat(result).isNotNull();
    }


}