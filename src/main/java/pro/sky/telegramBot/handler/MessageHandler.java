package pro.sky.telegramBot.handler;

import java.io.IOException;

public interface MessageHandler {
    void sendWelcomeMessage(String firstName, Long chatId) throws IOException;

    void sendDefaultMessage(Long chatId);

    void sendButtonMessage(Long chatId);
}
