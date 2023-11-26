package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.users.User;


// интерфейс сервиса для обработки запросов к БД пользователей и информиции о пользователях
public interface UserService {
    User findUserByChatId(Long chatId);

    User getById(Long id);
}
