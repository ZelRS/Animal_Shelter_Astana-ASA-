package pro.sky.telegramBot.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final Long USER_CHAT_ID = 123456789L;
    private User testUser;
    private UserInfo testUserInfo;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setChatId(USER_CHAT_ID);

        testUserInfo = new UserInfo();
        testUserInfo.setPhone("123456");

        when(userRepository.findByChatId(anyLong())).thenReturn(testUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
    }

    @Test
    void findUserByChatId_Should_ReturnUser() {
        User foundUser = userService.findUserByChatId(USER_CHAT_ID);
        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getById_Should_ReturnUser_When_UserExists() {
        User foundUser = userService.getById(USER_CHAT_ID);
        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getById_Should_ThrowException_When_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(USER_CHAT_ID));
    }

    @Test
    void create_Should_SaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.create(new User());
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }


}
