package com.example.technicaltask.controller;

import com.example.technicaltask.model.dto.request.MedicationRequestDto;
import com.example.technicaltask.repository.MedicationRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MedicationControllerTest {

    private static final String URL = "/api/v1/medications";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    public void MedicationController_CreateMedicationWithImage_ReturnIsCreated() throws Exception {
        //Given
        MedicationRequestDto medicationRequestDto = MedicationRequestDto.builder().name("Med-1").weight(50).build();
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpg", new byte[]{1, 2, 3, 4});
        medicationRequestDto.setImageFile(imageFile);

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL + "/create")
                        .file("imageFile", imageFile.getBytes())
                        .param("name", medicationRequestDto.getName())
                        .param("weight", String.valueOf(medicationRequestDto.getWeight())))
                .andExpect(status().isCreated());
    }

    @Test
    public void MedicationController_CreateMedicationWithOutImage_ReturnIsCreated() throws Exception {
        //Given
        MedicationRequestDto medicationRequestDto = MedicationRequestDto.builder().name("Med-1").weight(50).build();

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL + "/create")
                        .param("name", medicationRequestDto.getName())
                        .param("weight", String.valueOf(medicationRequestDto.getWeight())))
                .andExpect(status().isCreated());
    }

    @Test
    public void MedicationController_CreateMedicationWithNotValidName_ReturnBadRequest() throws Exception {
        //Given
        MedicationRequestDto medicationRequestDto = MedicationRequestDto.builder().name("Med 1").weight(100).build();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart(URL + "/create")
                .param("name", medicationRequestDto.getName())
                .param("weight", String.valueOf(medicationRequestDto.getWeight())));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Invalid name (Name should consists of letters, numbers, ‘-‘, ‘_’).");
    }

    @Test
    public void MedicationController_CreateMedicationWithNotValidWeight_ReturnBadRequest() throws Exception {
        //Given
        MedicationRequestDto medicationRequestDto = MedicationRequestDto.builder().name("Med-1").weight(-1).build();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart(URL + "/create")
                .param("name", medicationRequestDto.getName())
                .param("weight", String.valueOf(medicationRequestDto.getWeight())));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Weight must be positive value");
    }

    @Test
    public void MedicationController_CreateMedicationWithoutInputs_ReturnBadRequest() throws Exception {
        //Given
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/medications/create"));

        response.andExpect(status().isBadRequest());
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Name must not be null.");
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Name must not be empty.");
        assertThat(response.andReturn().getResponse().getContentAsString().trim()).contains("Weight must not be null.");
    }

    @Test
    public void MedicationController_GetAllMedications_ReturnIsOk() throws Exception {

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.totalElements", CoreMatchers.is(medicationRepository.findAll().size())));
    }


}