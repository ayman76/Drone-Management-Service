package com.example.technicaltask.service;

import com.example.technicaltask.exception.NotAvailableDroneException;
import com.example.technicaltask.exception.ResourceNotFoundException;
import com.example.technicaltask.exception.WeightLimitExceededException;
import com.example.technicaltask.model.Drone;
import com.example.technicaltask.model.Medication;
import com.example.technicaltask.model.Model;
import com.example.technicaltask.model.State;
import com.example.technicaltask.model.dto.request.DroneRequestDto;
import com.example.technicaltask.model.dto.request.LoadedMedicationRequestDto;
import com.example.technicaltask.model.dto.response.ApiResponse;
import com.example.technicaltask.model.dto.response.DroneResponseDto;
import com.example.technicaltask.model.dto.response.MedicationResponseDto;
import com.example.technicaltask.repository.DroneRepository;
import com.example.technicaltask.repository.MedicationRepository;
import com.example.technicaltask.service.impl.DroneServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DroneServiceImpl droneService;

    @BeforeEach
    public void setup() {
        droneService = new DroneServiceImpl(droneRepository, medicationRepository, modelMapper);
    }

    @Test
    public void DroneService_RegisterDrone_ReturnDroneResponseDto() {
        //Given
        DroneRequestDto droneRequestDto = DroneRequestDto.builder().model(0).batteryCapacity(100).build();
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();
        DroneResponseDto droneResponse = DroneResponseDto.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();

        when(modelMapper.map(Mockito.any(DroneRequestDto.class), Mockito.any())).thenReturn(drone);
        when(droneRepository.save(Mockito.any(Drone.class))).thenReturn(drone);
        when(modelMapper.map(Mockito.any(Drone.class), Mockito.any())).thenReturn(droneResponse);

        //When
        DroneResponseDto savedDrone = droneService.registerDrone(droneRequestDto);

        //Then
        assertThat(savedDrone).isNotNull();
        assertThat(savedDrone.getModel()).isEqualTo(drone.getModel());
        assertThat(savedDrone.getWeightLimit()).isEqualTo(drone.getWeightLimit());
        assertThat(savedDrone.getBatteryCapacity()).isEqualTo(drone.getBatteryCapacity());
        assertThat(savedDrone.getState()).isEqualTo(drone.getState());
        verify(droneRepository, times(1)).save(any());
        verify(modelMapper, times(2)).map(any(), any());

    }

    @Test
    public void DroneService_GetAllAvailableDrones_ReturnListOfDroneResponseDto() {
        //Given
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();
        Drone drone1 = Drone.builder().serialNumber("5d80b2de-3067-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Cruiserweight).build();

        DroneResponseDto droneResponse = DroneResponseDto.builder().serialNumber("5d80b2de-3068-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Middleweight).build();
        DroneResponseDto droneResponse1 = DroneResponseDto.builder().serialNumber("5d80b2de-3068-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Middleweight).build();


        List<Drone> mockDroneList = List.of(drone, drone1);
        Page<Drone> mockPage = new PageImpl<>(mockDroneList);

        when(droneRepository.findAllAvailableDrones(Mockito.any(Pageable.class), Mockito.any(State.class))).thenReturn(mockPage);

        List<DroneResponseDto> mockResponseList = List.of(droneResponse, droneResponse1);
        when(modelMapper.map(Mockito.any(Drone.class), Mockito.any())).thenReturn(mockResponseList.get(0));

        //When
        ApiResponse<DroneResponseDto> result = droneService.getAllAvailableDrones(0, 10);

        //Then
        Assertions.assertThat(result).isNotNull();
        verify(droneRepository, times(1)).findAllAvailableDrones(any(), any());
        verify(modelMapper, times(mockDroneList.size())).map(any(), any());
    }

    @Test
    public void DroneService_GetBatteryLevelForDrone_ReturnBatteryCapacity() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();
        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(drone));

        //When
        int batteryCapacity = droneService.getBatteryLevelForDrone(serialNumber);

        //Then
        assertThat(batteryCapacity).isEqualTo(drone != null ? drone.getBatteryCapacity() : null);
        verify(droneRepository, times(1)).findById(any());
    }

    @Test
    public void DroneService_GetBatteryLevelForDroneWithWrongPassword_ThrowResourceNotFoundedException() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";

        when(droneRepository.findById(Mockito.anyString())).thenThrow(ResourceNotFoundException.class);

        //Then
        assertThrows(ResourceNotFoundException.class, () -> droneService.getBatteryLevelForDrone(serialNumber));
        verify(droneRepository, times(1)).findById(any());
    }

    @Test
    public void DroneService_GetLoadedMedicationsOnDrone_ReturnApiResponseOfMedicationResponseDto() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();

        Medication medication = Medication.builder().name("Med-1").code("MED_CODE_0FAD1A").weight(20).imageUUID("IMG_B8A3B9").image(new byte[]{1, 2, 3}).build();
        Medication medication1 = Medication.builder().name("Med-2").code("MED_CODE_0FAD1B").weight(20).imageUUID("IMG_B8A3B8").image(new byte[]{1, 2, 3}).build();
        List<Medication> medicationList = List.of(medication, medication1);

        drone.setLoadedMedications(medicationList);


        MedicationResponseDto medicationResponseDto = MedicationResponseDto.builder().name("Med-1").code("MED_CODE_0FAD1A").weight(20).imageUUID("IMG_B8A3B9").build();
        MedicationResponseDto medicationResponseDto1 = MedicationResponseDto.builder().name("Med-2").code("MED_CODE_0FAD1B").weight(20).imageUUID("IMG_B8A3B8").build();

        List<MedicationResponseDto> medicationResponseList = List.of(medicationResponseDto, medicationResponseDto1);

        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.of(drone));
        when(modelMapper.map(Mockito.any(Medication.class), Mockito.any())).thenReturn(medicationResponseList.get(0));

        //When
        ApiResponse<MedicationResponseDto> response = droneService.getLoadedMedications(serialNumber, 0, 10);

        //Then
        Assertions.assertThat(response).isNotNull();
        verify(droneRepository, times(1)).findById(any());
        verify(modelMapper, times(medicationList.size())).map(any(), any());
    }

    @Test
    public void DroneService_LoadMedicationsToDrone_ReturnDroneResponseDto() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();

        LoadedMedicationRequestDto loadedMedicationRequestDto = LoadedMedicationRequestDto.builder().code("MED_CODE_0FAD1A").build();
        LoadedMedicationRequestDto loadedMedicationRequestDto1 = LoadedMedicationRequestDto.builder().code("MED_CODE_0FAD1B").build();
        List<LoadedMedicationRequestDto> loadedMedications = List.of(loadedMedicationRequestDto, loadedMedicationRequestDto1);

        Medication medication = Medication.builder().name("Med-1").code("MED_CODE_0FAD1A").weight(20).imageUUID("IMG_B8A3B9").image(new byte[]{1, 2, 3}).build();
        Medication medication1 = Medication.builder().name("Med-2").code("MED_CODE_0FAD1B").weight(20).imageUUID("IMG_B8A3B8").image(new byte[]{1, 2, 3}).build();
        List<Medication> medicationList = List.of(medication, medication1);

        drone.setLoadedMedications(medicationList);

        MedicationResponseDto medicationResponseDto = MedicationResponseDto.builder().name("Med-1").code("MED_CODE_0FAD1A").weight(20).imageUUID("IMG_B8A3B9").build();
        MedicationResponseDto medicationResponseDto1 = MedicationResponseDto.builder().name("Med-2").code("MED_CODE_0FAD1B").weight(20).imageUUID("IMG_B8A3B8").build();
        List<MedicationResponseDto> medicationResponseList = List.of(medicationResponseDto, medicationResponseDto1);

        DroneResponseDto droneResponseDto = DroneResponseDto.builder().serialNumber(drone.getSerialNumber()).state(drone.getState()).batteryCapacity(drone.getBatteryCapacity()).weightLimit(drone.getWeightLimit()).model(drone.getModel()).medications(medicationResponseList).build();

        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(Mockito.anyString())).thenReturn(Optional.of(medication));
        when(droneRepository.save(Mockito.any(Drone.class))).thenReturn(drone);
        when(modelMapper.map(Mockito.any(Drone.class), Mockito.any())).thenReturn(droneResponseDto);

        //When
        DroneResponseDto response = droneService.loadMedicationsToDrone(serialNumber, loadedMedications);

        //Then
        Assertions.assertThat(response).isNotNull();
        verify(droneRepository, times(1)).findById(any());
        verify(medicationRepository, times(loadedMedications.size())).findById(any());
        verify(droneRepository, times(2)).save(any());
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    public void DroneService_LoadMedicationsToDroneWithNotIdealState_ThrowNotAvailableDroneException() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.LOADED).weightLimit(200).model(Model.Lightweight).build();

        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.of(drone));

        //Then
        assertThrows(NotAvailableDroneException.class, () -> droneService.loadMedicationsToDrone(serialNumber, List.of(new LoadedMedicationRequestDto())));
        verify(droneRepository, times(1)).findById(any());
    }

    @Test
    public void DroneService_LoadMedicationsToDroneWithLowBatteryCapacity_ThrowNotAvailableDroneException() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(20).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();

        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.of(drone));

        //Then
        assertThrows(NotAvailableDroneException.class, () -> droneService.loadMedicationsToDrone(serialNumber, List.of(new LoadedMedicationRequestDto())));
        verify(droneRepository, times(1)).findById(any());

    }

    @Test
    public void DroneService_LoadMedicationsToDroneThatExceedWeightLimit_ThrowWeightLimitExceededException() {
        //Given
        String serialNumber = "5d80b2de-3066-49ad-96a1-9d5b1bcb7865";
        Drone drone = Drone.builder().serialNumber("5d80b2de-3066-49ad-96a1-9d5b1bcb7865").batteryCapacity(100).state(State.IDLE).weightLimit(200).model(Model.Lightweight).build();

        Medication medication = Medication.builder().name("Med-1").code("MED_CODE_0FAD1A").weight(150).imageUUID("IMG_B8A3B9").image(new byte[]{1, 2, 3}).build();
        Medication medication1 = Medication.builder().name("Med-2").code("MED_CODE_0FAD1B").weight(100).imageUUID("IMG_B8A3B8").image(new byte[]{1, 2, 3}).build();
        List<Medication> medicationList = List.of(medication, medication1);


        when(droneRepository.findById(Mockito.anyString())).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(Mockito.anyString())).thenReturn(Optional.of(medication));

        drone.setLoadedMedications(medicationList);

        //Then
        assertThrows(WeightLimitExceededException.class, () -> droneService.loadMedicationsToDrone(serialNumber, List.of(new LoadedMedicationRequestDto("MED_CODE_0FAD1A"), new LoadedMedicationRequestDto("MED_CODE_0FAD1B"))));
        verify(medicationRepository, times(2)).findById(any());
        verify(droneRepository, times(1)).findById(any());
    }


}