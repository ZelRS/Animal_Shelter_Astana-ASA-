package pro.sky.telegramBot.handler.usersActionHandlers;

import com.pengrad.telegrambot.model.Document;
import pro.sky.telegramBot.enums.UserState;

public interface DocumentHandler {
    void handle(Document document, Long chatId, UserState userState);
}
