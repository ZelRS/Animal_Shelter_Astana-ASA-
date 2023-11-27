package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.UserInfoRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.service.UserService;

// сервис для обработки запросов к БД пользователей и информиции о пользователях
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //  предполагается, что данным сервисом буду обрабатываться оба репозитория - User и UserInfo
    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    @Override
    public User findUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
}
