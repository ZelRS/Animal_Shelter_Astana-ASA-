package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null)
                .forEach(this::processUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        log.info("Processing update from user: {} {}", update.message().chat().firstName(), update.message().chat().lastName());
        //  получаем команду от пользователя
//        String text = update.message().text();
        //  получаем уникальный идентификатор чата, из которого отправлено сообщение
//        Long chatId = update.message().chat().id();
        //  получаем имя пользователя
//        String userName = update.message().chat().firstName();
        //  вызывается обработчик команд
//        commandHandlerService.handleCommand(chatId, userName, text);
    }
}
