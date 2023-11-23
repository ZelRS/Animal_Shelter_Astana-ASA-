package pro.sky.telegramBot.handler;

public interface MessageHandler {
    void sendWelcomeMessage(String firstName, Long chatId);

    void sendDefaultMessage(Long chatId);

    void sendButtonMessage(Long chatId);
}
