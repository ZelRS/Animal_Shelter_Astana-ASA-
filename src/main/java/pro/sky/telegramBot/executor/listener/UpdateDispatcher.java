package pro.sky.telegramBot.executor.listener;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.ButtonActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.CommandActionHandler;

/**
 * диспетчер принимает апдейты от слушателя и, проверяя на null, достает необходимые данные
 */
@Service
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final CommandActionHandler commandActionHandler;
    private final ButtonActionHandler buttonActionHandler;

    /**
     * главный метод диспетчера, организующий проверку на null данных вытянутых из команды пользователя
     */
    public void dispatch(Update update) {
        if (update.message() != null) {
            pullDataFromMessageCommand(update.message());
        } else if (update.callbackQuery() != null) {
            pullDataFromButtonCommand(update.callbackQuery());
        }
    }

    /**
     * метод достает из апдейта необходимые данные для формирования ответа<br>
     * при получении текстовой команды от пользователя.<br>
     * Далее данные отправляются в обработчик команд {@link #commandActionHandler}
     */
    private void pullDataFromMessageCommand(Message message) {

        String messageText = message.text();
        long chatId = message.chat().id();
        String firstName = message.chat().firstName();
        String lastName = message.chat().lastName();
        if (messageText != null) {
            commandActionHandler.handle(messageText, firstName, lastName, chatId);
        }
    }

    /**
     * метод достает из апдейта необходимые данные для формирования ответа<br>
     * при нажатии на кнопку пользователем.<br>
     * Далее данные отправляются в обработчик кнопок<br>
     * {@link #buttonActionHandler}
     */
    private void pullDataFromButtonCommand(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        String firstName = callbackQuery.from().firstName();
        String lastName = callbackQuery.from().lastName();
        buttonActionHandler.handle(callbackData, firstName, lastName, chatId);
    }
}
