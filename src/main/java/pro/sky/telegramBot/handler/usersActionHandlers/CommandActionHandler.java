package pro.sky.telegramBot.handler.usersActionHandlers;

import pro.sky.telegramBot.enums.UserState;

import java.io.IOException;

/**
 * интерфейс для управления обработкой команд пользователя
 */
public interface CommandActionHandler {
    void handle(String command, String firstName, String lastName, Long chatId, UserState userState);

}
