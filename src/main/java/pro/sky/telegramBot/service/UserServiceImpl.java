package pro.sky.telegramBot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.users.User;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    @Override
    public User findUserByChatId(Long chatId) {
        return null;
    }
}
