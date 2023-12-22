package pro.sky.telegramBot.service.servicesForInteractingWithRepositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static pro.sky.telegramBot.enums.UserState.FREE;
import static pro.sky.telegramBot.enums.UserState.VOLUNTEER;

/**
 * сервис для обработки запросов к БД пользователей и информации о пользователях
 */
//  предполагается, что данным сервисом буду обрабатываться оба репозитория - User и UserInfo
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    /**
     * получить пользователя из БД по chatId
     */
    @Override
    public User findUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    /**
     * получить пользователя из БД по id
     */
    @Override
    @Cacheable("users")
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * создать и сохранить пользователя в БД
     */
    @Override
    @CachePut(value = "users", key = "#user.id")
    public User createUserInfo(User user) {
        return userRepository.save(user);
    }

    /**
     * изменить пользователя в БД
     */
    @Override
    @CachePut(value = "users", key = "#user.id")
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод позволяет создать информацию о пользователе
     */
    @Override
    public UserInfo createUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    /**
     * Метод позволяет получить номер телефона пользователя
     */
    public Optional<String> getUserPhone(Long id) {
        return userRepository.findPhoneById(id);
    }

    /**
     * Метод позволяет получить список пользователей с выбранным статусом,
     * у которых отсутствует запись об усыновлении
     */
    @Override
    public List<User> findAllByAdoptionRecordIsNullAndState(UserState state) {
        return userRepository.findAllByAdoptionRecordIsNullAndState(state);
    }

    /**
     * Метод позволяет получить список пользователей с выбранным статусом
     */
    @Override
    public List<User> findAllByState(UserState userState) {
        return userRepository.findAllByState(userState);
    }

    /**
     * метод возвращает chatID случайного волонтера
     */
    public Long getRandomVolunteerId() {
        Random random = new Random();
        List<User> volunteersList = findAllByState(VOLUNTEER);
        if (!volunteersList.isEmpty()) {
            List<Long> volunteersIdList = volunteersList.stream()
                    .map(User::getChatId)
                    .collect(Collectors.toList());
            return volunteersIdList.get(random.nextInt(volunteersList.size()));
        } else {
            return 0L;
        }
    }

    /**
     * Метод добавляет номер телефона в таблицу UserInfo
     */
    public void addPhoneNumberToPersonInfo(String firstName, String lastName, Long chatId, String phone) {
        User user = findUserByChatId(chatId);
        UserInfo userInfo = createUserInfo(new UserInfo(firstName, lastName, phone));
        user.setUserInfo(userInfo);
        update(user);
    }

    /**
     * Метод позволяет сменить статус пользователя
     */
    @Override
    @CachePut(value = "users", key = "id")
    public void setUserState(Long id, UserState state) {
        User user = getById(id);
        user.setState(state);
        update(user);
    }

    /**
     * Метод позволяет получить список пользователей со статусом FREE
     */
    @Override
    public List<User> getFREESateUser() {
        return userRepository.findAllByState(FREE);
    }

    /**
     * Метод позволяет получить статус пользователя.
     * Если пользователь не найден, создается новый пользователь
     */
    @Override
    public UserState getUserState(Long chatId, String firstName) {
        UserState userState = FREE;
        User user = userRepository.findByChatId(chatId);
        if(user != null) {
            userState = user.getState();
        } else {
            createNewUser(chatId, firstName);
        }
        return userState;
    }


    /**
     * удаление пользователя по id
     */
    @Override
    @CacheEvict("users")
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Удаление невозможно. Пользователя по данному id не существует.");
        }
    }

    /**
     * получение списка всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Метод позволяет создать нового пользователя
     * с присваиванием ему статуса FREE и сохранением его в БД
     */
    private void createNewUser(Long chatId, String firstName) {
        User user = new User();
        user.setChatId(chatId);
        user.setUserName(firstName);
        user.setState(FREE);
        createUserInfo(user);
    }
}
