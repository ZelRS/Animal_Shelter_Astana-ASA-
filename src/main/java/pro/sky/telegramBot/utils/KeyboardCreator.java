package pro.sky.telegramBot.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.Buttons;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegramBot.enums.CallbackData.CATS_BUT;
import static pro.sky.telegramBot.enums.CallbackData.DOGS_BUT;

@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class KeyboardCreator {

    private final ButtonsCreator buttonsCreator;
    private final BotConfig config;

    public Keyboard catsAndDogsMessageKeyboard() {
        log.info("Creating keyboard markup for cats and dogs");

        List<Buttons> buttons = new ArrayList<>();
        buttons.add(new Buttons(config.getCats_but(), CATS_BUT.getCallbackData()));
        buttons.add(new Buttons(config.getDogs_but(), DOGS_BUT.getCallbackData()));
        return buttonsCreator.sendWithInlineKeyboard(buttons);
    }
}
