package pro.sky.telegramBot.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pro.sky.telegramBot.enums.UserState.VOLUNTEER;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;

import java.util.*;

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
    void findUserByChatId_Should_ReturnUser_Test() {
        User foundUser = userService.findUserByChatId(USER_CHAT_ID);
        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getById_Should_ReturnUser_When_UserExists_Test() {
        User foundUser = userService.getById(USER_CHAT_ID);
        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getById_Should_ThrowException_When_UserNotFound_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(USER_CHAT_ID));
    }

    @Test
    void create_Should_SaveUser_Test() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.createUserInfo(new User());
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void update_Should_ReturnUpdatedUser_Test() {
        User updatedUser = new User();
        updatedUser.setChatId(USER_CHAT_ID);
        updatedUser.setState(VOLUNTEER);

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(testUser);
        assertNotNull(result);
        assertEquals(updatedUser.getState(), result.getState());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void findAllByAdoptionRecordIsNullAndState_Should_ReturnUsers_Test() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(userRepository.findAllByAdoptionRecordIsNullAndState(any(UserState.class)))
                .thenReturn(userList);

        List<User> foundUsers = userService.findAllByAdoptionRecordIsNullAndState(UserState.FREE);
        assertFalse(foundUsers.isEmpty());
        assertEquals(userList.size(), foundUsers.size());
        assertTrue(foundUsers.contains(testUser));
    }

    @Test
    void getRandomVolunteerId_Should_ReturnValidId_Test() {
        List<User> volunteersList = Collections.singletonList(testUser);

        when(userRepository.findAllByState(VOLUNTEER)).thenReturn(volunteersList);

        Long id = userService.getRandomVolunteerId();
        assertNotNull(id);
        assertEquals(testUser.getChatId(), id);

    }

    @Test
    void setUserState_Should_ChangeUserState_Test() {
        UserState newState = UserState.PROBATION;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.setUserState(USER_CHAT_ID, newState);

        verify(userRepository, times(1)).save(testUser);

        assertEquals(newState, testUser.getState());
    }

}
