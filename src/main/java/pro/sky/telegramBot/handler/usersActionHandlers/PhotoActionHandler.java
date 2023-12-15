package pro.sky.telegramBot.handler.usersActionHandlers;

import com.pengrad.telegrambot.model.PhotoSize;
import pro.sky.telegramBot.enums.UserState;

public interface PhotoActionHandler {
    void handle(PhotoSize[] photo, Long chatId, UserState userState);
}
