package pro.sky.telegramBot.handler;

public interface Handler {
    void handle(String callbackData, String firstName, String lastName, Long chatId);
}
