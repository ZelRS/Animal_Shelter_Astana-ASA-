package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.ButtonActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.CommandActionHandler;

// диспетчер принимает апдейты отслушателя и, проверяя на null, достает необходимые данные
@Service
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final CommandActionHandler commandActionHandler;
    private final ButtonActionHandler buttonActionHandler;

    // главный метод диспетчера, орагинзующий проверку на null данных вытянутых из команды пользователя
    public void dispatch(Update update) {
        if (update.message() != null) {
            pullDataFromMessageCommand(update.message());
        } else if (update.callbackQuery() != null) {
            pullDataFromButtonCommand(update.callbackQuery());
        }
    }

    // метод достает из апдейта данные необходимые для формирования текстового ответа
    // и отправляет их в обработчик команд
    private void pullDataFromMessageCommand(Message message) {

        String messageText = message.text();
        long chatId = message.chat().id();
        String firstName = message.chat().firstName();
        String lastName = message.chat().lastName();
        if (messageText != null) {
            commandActionHandler.handle(messageText, firstName, lastName, chatId);
        }
    }

    // метод достает из апдейта данные необходимые для формирования ответа при нажатии кнопки пользователем
    // и отправляет их в обработчик кнопок
    private void pullDataFromButtonCommand(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        String firstName = callbackQuery.from().firstName();
        String lastName = callbackQuery.from().lastName();
        buttonActionHandler.handle(callbackData, firstName, lastName, chatId);
    }
}
