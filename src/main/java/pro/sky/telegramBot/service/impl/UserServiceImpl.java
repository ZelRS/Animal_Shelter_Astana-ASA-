package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public User findUserByChatId(Long chatId) {
        return null;
    }
}
