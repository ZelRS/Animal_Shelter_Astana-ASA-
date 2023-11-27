package pro.sky.telegramBot.utils.keyboardUtils;

import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.Button;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegramBot.enums.CallbackData.*;

// класс получает логику создания конкретной однострочной клавиатуры и внедряет свойства кнопкам
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificKeyboardCreator {

    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final BotConfig config;

    // метод формирует ряд определенных кнопок для сообщения, в котором отражается возможность
    // пользователю выбрать тип животного, которое он хочет
    public Keyboard petSelectionMessageKeyboard() {
        log.info("Creating keyboard markup for cats and dogs");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_WANT_CAT(), BUT_WANT_CAT.getCallbackData()));
        buttons.add(new Button(config.getBUT_WANT_DOG(), BUT_WANT_DOG.getCallbackData()));
        return inlineKeyboardCreator.createInlineKeyboard(buttons);
    }

    public Keyboard shelterFunctionalMessageKeyboard() {
        log.info("Creating keyboard markup for shelters functional");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_WANT_TAKE_PET(), BUT_WANT_TAKE_PET.getCallbackData()));
        buttons.add(new Button(config.getBUT_SEND_REPORT(), BUT_SEND_REPORT.getCallbackData()));
        buttons.add(new Button(config.getBUT_CALL_VOLUNTEER(), BUT_CALL_VOLUNTEER.getCallbackData()));
        return inlineKeyboardCreator.createInlineKeyboard(buttons);
    }

//    ...... клавиатуры для других типов сообщений.....

}
