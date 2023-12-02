package pro.sky.telegramBot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * слушатель апдейтов в боте
 */
@Component
@RequiredArgsConstructor
public class UpdateListener {
    private final UpdateDispatcher updateDispatcher;
    private final TelegramBot telegramBot;

    /**
     * для каждого апдейта применяется метод dispatch() класса диспетчеризации updateDispatcher
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(updateDispatcher::dispatch);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
