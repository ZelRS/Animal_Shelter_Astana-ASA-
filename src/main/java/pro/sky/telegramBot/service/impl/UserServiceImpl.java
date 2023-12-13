package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * создать и сохранить пользователя в БД
     */
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    /**
     * изменить пользователя в БД
     */
    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserInfo create(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    public Optional<String> getUserPhone(Long id) {
        return userRepository.findPhoneById(id);
    }

    public UserInfo setUserPhone(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public List<User> findAllByAdoptionRecordIsNullAndState(UserState state) {
        return userRepository.findAllByAdoptionRecordIsNullAndState(state);
    }

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
        UserInfo userInfo = setUserPhone(new UserInfo(firstName, lastName, phone));
        user.setUserInfo(userInfo);
        update(user);
    }

    @Override
    public void setUserState(Long id, UserState state) {
        User user = getById(id);
        user.setState(state);
        update(user);
    }
}
