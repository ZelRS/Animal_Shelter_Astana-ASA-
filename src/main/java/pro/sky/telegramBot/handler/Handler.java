package pro.sky.telegramBot.handler;

import java.io.IOException;

public interface Handler {
    void handle(String callbackData, String firstName, String lastName, Long chatId) throws IOException;
}
