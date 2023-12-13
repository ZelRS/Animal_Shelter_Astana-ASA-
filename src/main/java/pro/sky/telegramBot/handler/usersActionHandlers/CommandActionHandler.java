package pro.sky.telegramBot.handler.usersActionHandlers;

import java.io.IOException;

/**
 * интерфейс для управления обработкой команд пользователя
 */
public interface CommandActionHandler {
    void handle(String callbackData, String firstName, String lastName, Long chatId) throws IOException;

}
