package pro.sky.telegramBot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.telegramBot.controller.PetController;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.service.PetService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

    @Test
    void testCreatePet() throws Exception {
        Pet pet = new Pet();
        when(petService.create(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUploadPhoto() throws Exception {
        Long petId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        mockMvc.perform(multipart("/pet/{id}", petId)
                        .file(file))
                .andExpect(status().isOk());

        verify(petService, times(1)).uploadPhoto(eq(petId), any());
    }

    @Test
    void testUpdatePet() throws Exception {
        Pet pet = new Pet();
        when(petService.update(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(put("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetPetById() throws Exception {
        Long petId = 1L;
        Pet pet = new Pet();
        when(petService.getById(eq(petId))).thenReturn(pet);

        mockMvc.perform(get("/pet/{id}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
}



