package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateDispatcher {
    private final CommandActionHandlerImpl commandActionHandler;
    private final ButtonActionHandlerImpl buttonActionHandler;
    private final DocumentActionHandler documentActionHandler;
    private final PhotoActionHandler photoActionHandler;

    public void dispatch(Update update) {
        if (update == null) return;

        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();

        // Вызываем соответствующий обработчик в зависимости от контента апдейта.
        if (message != null) {
            handleIncomingMessage(message);
        } else if (callbackQuery != null) {
            handleCallbackQuery(callbackQuery);
        }
    }

    private void handleIncomingMessage(Message message) {
        Long chatId = message.chat().id();
        if (message.document() != null) {
            log.info("Invoking document handler for chatId: {}", chatId);
            documentActionHandler.handle(message.document(), chatId);
        } else if (message.photo() != null) {
            log.info("Invoking photo handler for chatId: {}", chatId);
            photoActionHandler.handle(message.photo(), chatId);
        } else {
            log.info("Invoking message command handler for chatId: {}", chatId);
            handleMessageCommand(message);
        }
    }

    private void handleMessageCommand(Message message) {
        String messageText = message.text();
        if (messageText != null) {
            Chat chat = message.chat();
            commandActionHandler.handle(messageText, chat.firstName(), chat.lastName(), chat.id());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        Message message = callbackQuery.message();

        if (message != null) {
            log.info("Invoking button handler for chatId: {}", message.chat().id());
            buttonActionHandler.handle(
                    callbackData,
                    callbackQuery.from().firstName(),
                    callbackQuery.from().lastName(),
                    message.chat().id(),
                    message.chat().username()
            );
        }
    }
}
