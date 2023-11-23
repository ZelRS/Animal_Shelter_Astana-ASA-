package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.impl.ButtonHandler;
import pro.sky.telegramBot.handler.impl.CommandHandler;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final CommandHandler commandHandler;
    private final ButtonHandler buttonHandler;

    public void dispatch(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }


    }

    private void handleMessage(Message message) {
        String messageText = message.text();
        long chatId = message.chat().id();
        String firstName = message.chat().firstName();
        String lastName = message.chat().lastName();
        if (messageText != null) {
            commandHandler.handle(messageText, firstName, lastName, chatId);
        }
    }
    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        String firstName = callbackQuery.from().firstName();
        String lastName = callbackQuery.from().lastName();
        buttonHandler.handle(callbackData, firstName, lastName, chatId);
    }
}
