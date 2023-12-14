package pro.sky.telegramBot.controller;

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
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void testGetById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setState(UserState.FREE);
        user.setChatId(999L);
        user.setUserName("Name");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.state").value("FREE"))
                .andExpect(jsonPath("$.chatId").value(user.getChatId()));
    }

}
