package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.users.User;

public interface UserService {
    User findUserByChatId(Long chatId);
}
