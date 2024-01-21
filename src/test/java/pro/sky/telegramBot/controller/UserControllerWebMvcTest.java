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
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.telegramBot.controller.constants.UserControllerTestConstants.*;

@WebMvcTest(UserController.class)
public class UserControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserInfoRepository userInfoRepository;

    @SpyBean
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        USER.setId(USER_ID);
        USER.setChatId(USER_CHAT_ID);
        USER.setUserName(USER_NAME);
        USER.setState(USER_STATE);
    }

    /**
     * проверка поиска пользователя по id
     */
    @Test
    void testGetById() throws Exception {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", USER.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER.getId()))
                .andExpect(jsonPath("$.userName").value(USER.getUserName()))
                .andExpect(jsonPath("$.state").value("FREE"))
                .andExpect(jsonPath("$.chatId").value(USER.getChatId()));
    }

    /**
     * если пользователя по id нет, должна выбрасываться ошибка UserNotFoundException()
     */
    @Test
    void testGetByIdUserNotFoundException() throws Exception {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", USER.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    /**
     * проверка создания нового пользователя
     */
    @Test
    void testCreate() throws Exception {
        JSONObject jsonObject = new JSONObject();

        when(userRepository.save(any(User.class))).thenReturn(USER);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER.getId()))
                .andExpect(jsonPath("$.userName").value(USER.getUserName()))
                .andExpect(jsonPath("$.state").value("FREE"))
                .andExpect(jsonPath("$.chatId").value(USER.getChatId()));

    }

    /**
     * проверка корректного изменения статуса пользователя
     */
    @Test
    void testSetUserState() throws Exception {

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));
        USER.setState(ANOTHER_USER_STATE);
        UserState expectedSate = UserState.POTENTIAL;
        when(userRepository.save(any(User.class))).thenReturn(USER);


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/{id}", USER.getId())
                        .param("state", "POTENTIAL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(expectedSate, USER.getState());
    }

}
