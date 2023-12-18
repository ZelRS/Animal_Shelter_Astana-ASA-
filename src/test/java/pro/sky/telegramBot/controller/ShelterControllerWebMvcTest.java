package pro.sky.telegramBot.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegramBot.exception.notFound.ShelterNotFoundException;
import pro.sky.telegramBot.service.handlers.specificHandlers.impl.ShelterCommandHandler;
import pro.sky.telegramBot.service.loaders.MediaLoader;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.impl.ShelterServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.telegramBot.controller.constants.ShelterControllerTestConstants.*;

@WebMvcTest(ShelterController.class)
public class ShelterControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelterRepository shelterRepository;
    @MockBean
    private MediaLoader mediaLoader;

    @SpyBean
    private ShelterServiceImpl shelterService;
    @MockBean
    private ShelterCommandHandler shelterCommandHandler;

    @InjectMocks
    private ShelterController shelterController;

    @BeforeEach
    void setUp() {
        SHELTER.setId(ID);
        SHELTER.setName(NAME);
        SHELTER.setDescription(DESCRIPTION);
        SHELTER.setPreview(PREVIEW);
        SHELTER.setAddress(ADDRESS);
        SHELTER.setSchedule(SCHEDULE);
        SHELTER.setSecurityPhone(SECURITY_PHONE);
        SHELTER.setSafetyRules(SAFETY_RULES);
        SHELTER.setType(TYPE);
    }

    /**
     * проверка создания нового приюта
     */
    @Test
    void testCreateShelter() throws Exception {
        JSONObject jsonObject = new JSONObject();

        when(shelterRepository.save(any(Shelter.class))).thenReturn(SHELTER);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.name").value(SHELTER.getName()))
                .andExpect(jsonPath("$.description").value(SHELTER.getDescription()))
                .andExpect(jsonPath("$.preview").value(SHELTER.getPreview()))
                .andExpect(jsonPath("$.schedule").value(SHELTER.getSchedule()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()))
                .andExpect(jsonPath("$.securityPhone").value(SHELTER.getSecurityPhone()))
                .andExpect(jsonPath("$.safetyRules").value(SHELTER.getSafetyRules()))
                .andExpect(jsonPath("$.type").value("DOG"));
    }

    /**
     * проверка загрузки фото для приюта
     */
    @Test
    void testUploadPhoto() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.of(SHELTER));

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/shelter/{id}", SHELTER.getId())
                        .file(PHOTO))
                .andExpect(status().isOk());

        assertEquals(BYTES, SHELTER.getData());
    }

    /**
     * проверка выбрасывания исключения при загрузке фото приюта, если приют по id не найден
     */
    @Test
    void testUploadPhotoWithShelterNotFoundException() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/shelter/{id}", SHELTER.getId())
                        .file(PHOTO))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ShelterNotFoundException));
    }

    /**
     * проверка загрузки схемы проезда приюта
     */
    @Test
    void testUploadSchema() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.of(SHELTER));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/shelter/{id}/schema", SHELTER.getId())
                        .file(SCHEMA))
                .andExpect(status().isOk());
    }

    /**
     * проверка выбрасывания исключения при загрузке схемы проезда приюта,<br>
     * если приют по id не найден
     */
    @Test
    void testUploadSchemaWithShelterNotFoundException() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/shelter/{id}/schema", SHELTER.getId())
                        .file(SCHEMA))
                .andExpect(status().isNotFound());
    }

    /**
     * проверка поиска приюта по id
     */
    @Test
    void testGetById() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.of(SHELTER));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/{id}", SHELTER.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.name").value(SHELTER.getName()))
                .andExpect(jsonPath("$.description").value(SHELTER.getDescription()))
                .andExpect(jsonPath("$.preview").value(SHELTER.getPreview()))
                .andExpect(jsonPath("$.schedule").value(SHELTER.getSchedule()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()))
                .andExpect(jsonPath("$.securityPhone").value(SHELTER.getSecurityPhone()))
                .andExpect(jsonPath("$.safetyRules").value(SHELTER.getSafetyRules()))
                .andExpect(jsonPath("$.type").value("DOG"));
    }

    /**
     * проверка выбрасывания исключения при поиске приюта,<br>
     * если приют по id не найден
     */
    @Test
    void testGetByIdWithShelterNotFoundException() throws Exception {
        when(shelterRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/{id}", SHELTER.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ShelterNotFoundException));
    }

    /**
     * проверка обновления существующего приюта
     */
    @Test
    void testUpdate() throws Exception {
        JSONObject jsonObject = new JSONObject();
        when(shelterRepository.save(any(Shelter.class))).thenReturn(SHELTER);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(SHELTER.getName()))
                .andExpect(jsonPath("$.description").value(SHELTER.getDescription()))
                .andExpect(jsonPath("$.preview").value(SHELTER.getPreview()))
                .andExpect(jsonPath("$.schedule").value(SHELTER.getSchedule()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()))
                .andExpect(jsonPath("$.securityPhone").value(SHELTER.getSecurityPhone()))
                .andExpect(jsonPath("$.safetyRules").value(SHELTER.getSafetyRules()))
                .andExpect(jsonPath("$.type").value("DOG"));
    }
}
