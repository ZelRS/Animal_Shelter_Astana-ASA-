package pro.sky.telegramBot.handler.usersActionHandlers;

import pro.sky.telegramBot.enums.UserState;

import java.io.IOException;

/**
 * интерфейс для управления обработкой кнопок пользователя
 */
public interface ButtonActionHandler {
    void handle(String callbackData, String firstName, String lastName, Long chatId, String username, UserState userState);
}
