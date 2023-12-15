package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.handler.usersActionHandlers.ButtonActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.CommandActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateDispatcher {
    private final CommandActionHandler commandActionHandler;
    private final ButtonActionHandler buttonActionHandler;
    private final DocumentActionHandler documentActionHandler;
    private final PhotoActionHandlerImpl photoActionHandlerImpl;

    public void handleIncomingMessage(Message message, Long chatId, UserState userState) {
        if (message.document() != null) {
            log.info("Invoking document handler for chatId: {}", chatId);
            documentActionHandler.handle(message.document(), chatId, userState);
        } else if (message.photo() != null) {
            log.info("Invoking photo handler for chatId: {}", chatId);
            photoActionHandlerImpl.handle(message.photo(), chatId, userState);
        } else {
            log.info("Invoking message command handler for chatId: {}", chatId);
            handleMessageCommand(message, chatId, userState);
        }
    }

    public void handleMessageCommand(Message message, Long chatId, UserState userState) {
        String messageText = message.text();
        if (messageText != null) {
            Chat chat = message.chat();
            commandActionHandler.handle(messageText, chat.firstName(), chat.lastName(), chatId, userState);
        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery, Long chatId, UserState userState) {
        String callbackData = callbackQuery.data();
        Message message = callbackQuery.message();

        if (message != null) {
            log.info("Invoking button handler for chatId: {}", message.chat().id());
            buttonActionHandler.handle(
                    callbackData,
                    callbackQuery.from().firstName(),
                    callbackQuery.from().lastName(),
                    chatId,
                    message.chat().username(),
                    userState
            );
        }
    }
}
