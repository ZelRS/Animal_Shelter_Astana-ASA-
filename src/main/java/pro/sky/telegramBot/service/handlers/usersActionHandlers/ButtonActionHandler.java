package pro.sky.telegramBot.service.handlers.usersActionHandlers;

import pro.sky.telegramBot.enums.UserState;

/**
 * интерфейс для управления обработкой кнопок пользователя
 */
public interface ButtonActionHandler {
    void handle(String callbackData, String firstName, String lastName, Long chatId, String username, UserState userState);
}
