package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// слушатель апдейтов боте
@Component
@RequiredArgsConstructor
public class UpdateListener {
    private final UpdateDispatcher updateDispatcher;
    private final TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            // для каждого апдейта применяется метод dispatch() класса диспетчеризации апдейтов
            updates.forEach(updateDispatcher::dispatch);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
