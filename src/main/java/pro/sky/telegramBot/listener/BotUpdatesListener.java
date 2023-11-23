package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.impl.ButtonHandler;
import pro.sky.telegramBot.handler.impl.CommandHandler;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final CommandHandler commandHandler;
    private final ButtonHandler buttonHandler;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);

            if (update.message() != null) {
                String messageText = update.message().text();
                long chatId = update.message().chat().id();
                String firstName = update.message().chat().firstName();
                String lastName = update.message().chat().lastName();

                if (messageText != null) {
                    commandHandler.handle(messageText, firstName, lastName, chatId);
                }
            }

            if (update.callbackQuery() != null) {
                String callbackData = update.callbackQuery().data();
                long chatId = update.callbackQuery().message().chat().id();
                String firstName = update.callbackQuery().from().firstName();
                String lastName = update.callbackQuery().from().lastName();
                buttonHandler.handle(callbackData, firstName, lastName, chatId);
            }
        });
        log.info("Updates processed");
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
