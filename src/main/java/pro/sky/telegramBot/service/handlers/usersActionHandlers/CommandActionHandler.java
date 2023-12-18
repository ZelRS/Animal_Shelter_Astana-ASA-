package pro.sky.telegramBot.service.handlers.usersActionHandlers;

import pro.sky.telegramBot.enums.UserState;

/**
 * интерфейс для управления обработкой команд пользователя
 */
public interface CommandActionHandler {
    void handle(String command, String firstName, String lastName, Long chatId, UserState userState);

}
