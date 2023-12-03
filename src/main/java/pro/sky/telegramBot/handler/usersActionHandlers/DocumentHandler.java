package pro.sky.telegramBot.handler.usersActionHandlers;

import com.pengrad.telegrambot.model.Document;

public interface DocumentHandler {
    void handle(Document document, Long chatId);
}
