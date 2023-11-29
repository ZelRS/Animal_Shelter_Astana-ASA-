package pro.sky.telegramBot.handler.usersActionHandlers;

import java.io.IOException;

/**
 * интерфейс для управления обработкой действий пользователя
 */
public interface ActionHandler {
    void handle(String callbackData, String firstName, String lastName, Long chatId) throws IOException;

}
