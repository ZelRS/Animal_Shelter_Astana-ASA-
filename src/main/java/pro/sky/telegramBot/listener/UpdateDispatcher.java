package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.ButtonActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.CommandActionHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.DocumentActionHandler;

import java.io.InputStream;

/**
 * диспетчер принимает апдейты от слушателя и, проверяя на null, достает необходимые данные
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class UpdateDispatcher {
    private final CommandActionHandler commandActionHandler;
    private final ButtonActionHandler buttonActionHandler;
    private final DocumentActionHandler documentActionHandler;

    /**
     * главный метод диспетчера, организующий проверку на null данных вытянутых из команды пользователя
     */
    public void dispatch(Update update) {
        if (update.message() != null && update.message().document() != null) {
            pullDataFromDocument(update.message());
        } else if (update.message() != null) {
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
        log.info("Was invoked method pullDataFromMessageCommand");
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
        log.info("Was invoked method pullDataFromButtonCommand");
        String callbackData = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        String firstName = callbackQuery.from().firstName();
        String lastName = callbackQuery.from().lastName();
        buttonActionHandler.handle(callbackData, firstName, lastName, chatId);
    }
    /**
     * Метод достает из апдейта необходимые данные для формирования ответа<br>
     * при получении документа от пользователя.<br>
     * Далее данные отправляются в обработчик документов {@link #documentActionHandler}
     */
    private void pullDataFromDocument(Message message) {
        log.info("Was invoked method pullDataFromDocument");
        Document document = message.document();
        long chatId = message.chat().id();
        if (document != null) {
            documentActionHandler.handle(document, chatId);
        }
    }

}
