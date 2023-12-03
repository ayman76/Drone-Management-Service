package com.example.technicaltask.controller;

import com.example.technicaltask.model.Drone;
import com.example.technicaltask.model.Medication;
import com.example.technicaltask.model.Model;
import com.example.technicaltask.model.State;
import com.example.technicaltask.model.dto.request.DroneRequestDto;
import com.example.technicaltask.model.dto.request.LoadedMedicationRequestDto;
import com.example.technicaltask.repository.DroneRepository;
import com.example.technicaltask.repository.MedicationRepository;
import com.example.technicaltask.service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DroneControllerTest {

    private static final String URL = "/api/v1/drone";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private DroneService droneService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void DroneController_RegisterDrone_ReturnIsCreated() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().model(0).batteryCapacity(100).build();

        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", CoreMatchers.is(drone.getModel().toString())))
                .andExpect(jsonPath("$.weightLimit", CoreMatchers.is(drone.getWeightLimit())))
                .andExpect(jsonPath("$.state", CoreMatchers.is(drone.getState().toString())));
    }

    @Test
    public void DroneController_RegisterDroneWithNoInputs_ReturnIsBadRequest() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().build();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Battery Capacity should not be empty.");
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Model should not be empty.");
    }

    @Test
    public void DroneController_RegisterDroneWithNotValidModel_ReturnIsBadRequest() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().model(5).batteryCapacity(100).build();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", CoreMatchers.is("Model not founded.")));
    }

    @Test
    public void DroneController_RegisterDroneWithNegativeBatteryCapacity_ReturnIsBadRequest() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().model(1).batteryCapacity(-1).build();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", CoreMatchers.is("Battery Capacity Should be Positive value.")));

    }

    @Test
    public void DroneController_RegisterDroneWithNotValidModelAndBatteryCapacity_ReturnIsBadRequest() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().model(5).batteryCapacity(-1).build();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Battery Capacity Should be Positive value.");
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Model not founded.");
    }

    @Test
    public void DroneController_RegisterDroneWithBatteryCapacityGreaterThan100_ReturnIsBadRequest() throws Exception {
        //Given
        DroneRequestDto droneRequest = DroneRequestDto.builder().model(1).batteryCapacity(200).build();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(droneRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", CoreMatchers.is("Battery Capacity can not exceed 100%.")));

    }

    @Test
    public void DroneController_GetAllAvailableDrones_ReturnIsOK() throws Exception {
        //Given
        List<Drone> drones = droneRepository.findAll().stream().sorted(Comparator.comparing(Drone::getState)).sorted(Comparator.comparing(Drone::getBatteryCapacity)).toList();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/available-for-loading")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk()).andExpect(jsonPath("$.content.size()", CoreMatchers.is(drones.size())));
    }

    @Test
    public void DroneController_GetBatteryLevelForDrone_ReturnIsOK() throws Exception {
        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();
        Drone savedDrone = droneRepository.save(drone);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + savedDrone.getSerialNumber() + "/battery-level")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(savedDrone.getBatteryCapacity())));
    }

    @Test
    public void DroneController_GetLoadedMedications_ReturnIsOK() throws Exception {
//        Drone drone = droneRepository.findAll().stream().filter(d -> d.getState() == State.IDLE).findFirst().get();

        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).model(Model.Lightweight).weightLimit(Model.Lightweight.getValue()).loadedMedications(new ArrayList<>()).build();

        Drone savedDrone = droneRepository.save(drone);

        Medication medication = Medication.builder().name("Med-test").weight(10).build();
        Medication savedMedication = medicationRepository.save(medication);

        droneService.loadMedicationsToDrone(savedDrone.getSerialNumber(), List.of(new LoadedMedicationRequestDto(savedMedication.getCode())));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + drone.getSerialNumber() + "/loaded-medications")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(savedDrone.getLoadedMedications().size())));
    }


    @Test
    public void DroneController_LoadMedications_ReturnIsOK() throws Exception {
        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();

        Drone savedDrone = droneRepository.save(drone);

        Medication medication = Medication.builder().name("Med-test").weight(10).build();
        Medication medication1 = Medication.builder().name("Med-test1").weight(10).build();
        List<Medication> savedMedication = medicationRepository.saveAll(List.of(medication, medication1));

        List<LoadedMedicationRequestDto> loadedMedicationRequest = savedMedication.stream().map(m -> modelMapper.map(m, LoadedMedicationRequestDto.class)).toList();

        savedDrone.setLoadedMedications(savedMedication);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/" + savedDrone.getSerialNumber() + "/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadedMedicationRequest)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.medications.size()", CoreMatchers.is(savedDrone.getLoadedMedications().size())));
    }


    @Test
    public void DroneController_LoadMedicationsWithEmptyCode_ReturnIsBadRequest() throws Exception {
        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();

        Drone savedDrone = droneRepository.save(drone);


        List<LoadedMedicationRequestDto> loadedMedicationRequest = List.of(new LoadedMedicationRequestDto(""));


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/" + savedDrone.getSerialNumber() + "/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadedMedicationRequest)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", CoreMatchers.is("Medication Code could not be empty.")));
    }

    @Test
    public void DroneController_LoadMedicationsWithNullCode_ReturnIsBadRequest() throws Exception {
        Drone drone = Drone.builder().state(State.IDLE).batteryCapacity(100).weightLimit(Model.Lightweight.getValue()).model(Model.Lightweight).build();

        Drone savedDrone = droneRepository.save(drone);


        List<LoadedMedicationRequestDto> loadedMedicationRequest = List.of(new LoadedMedicationRequestDto(null));


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(URL + "/" + savedDrone.getSerialNumber() + "/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadedMedicationRequest)));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Medication Code could not be empty.");
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Medication Code could not be null.");
    }


}