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
import java.util.Optional;

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

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


}
