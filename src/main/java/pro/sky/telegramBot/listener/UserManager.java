package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.UserService;

/**
 * Определяет, есть ли в базе пользователь и какой у него статус. Если пользователя нет, то вносит его в базу.
 */
@Component
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class UserManager {
    private final UpdateDispatcher updateDispatcher;
    private final UserService userService;
    public void dispatch(Update update) {
        if (update == null) return;

        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();

        Long chatId;
        UserState userState;
        String userName;

        // Вызываем соответствующий обработчик в зависимости от контента апдейта.
        if (message != null) {
            chatId = message.chat().id();
            userName = message.chat().firstName();
            userState = userService.getUserState(chatId, userName);
            updateDispatcher.handleIncomingMessage(message, chatId, userState);
        } else if (callbackQuery != null) {
            chatId = callbackQuery.message().chat().id();
            userName = callbackQuery.message().chat().firstName();
            userState = userService.getUserState(chatId, userName);
            updateDispatcher.handleCallbackQuery(callbackQuery, chatId, userState);
        }
    }
}
