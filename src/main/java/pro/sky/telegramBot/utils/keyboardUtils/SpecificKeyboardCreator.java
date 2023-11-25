package pro.sky.telegramBot.utils.keyboardUtils;

import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.Button;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegramBot.enums.CallbackData.CAT_BUT;
import static pro.sky.telegramBot.enums.CallbackData.DOG_BUT;

// класс получает логику создания однострочной клавиатуры и совместно со свойствами кнопок применяет ее для конкретного
// сообщения
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificKeyboardCreator {

    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final BotConfig config;

    // метод формирует ряд определенных кнопок для сообщения, в котором отражается возможность
    // пользователю выбратьт тип животного, которое он хочет
    public Keyboard petSelectionMessageKeyboard() {
        log.info("Creating keyboard markup for cats and dogs");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getCats_but(), CAT_BUT.getCallbackData()));
        buttons.add(new Button(config.getDogs_but(), DOG_BUT.getCallbackData()));
        return inlineKeyboardCreator.createInlineKeyboard(buttons);
    }

//    ...... клавиатуры для других типов сообщений.....

}
