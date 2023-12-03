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

/**
 * методы класса получают логику создания конкретной однострочной<br>
 * клавиатуры и задают свойства кнопкам
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificKeyboardCreator {

    private final KeyboardCreator keyboardCreator;
    private final BotConfig config;

    /**
     * метод формирует для сообщения ряд определенных кнопок,<br>
     * с помощью которых пользователь может выбрать тип животного,<br>
     * которым он интересуется
     */
    public Keyboard petSelectionMessageKeyboard() {
        log.info("Creating keyboard markup for cats and dogs");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_WANT_CAT(), BUT_WANT_CAT.getCallbackData()));
        buttons.add(new Button(config.getBUT_WANT_DOG(), BUT_WANT_DOG.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }


    /**
     * метод формирует для сообщения ряд определенных кнопок ,<br>
     * в которых отражается функционал приюта, выбранного пользователем
     */
    public Keyboard shelterFunctionalMessageKeyboard() {
        log.info("Creating keyboard markup for shelters functional");

        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_GET_FULL_INFO(), BUT_GET_FULL_INFO.getCallbackData()));
        buttons.add(new Button(config.getBUT_TAKING_PET(), BUT_TAKING_PET.getCallbackData()));
        buttons.add(new Button(config.getBUT_SEND_REPORT(), BUT_SEND_REPORT.getCallbackData()));
        buttons.add(new Button(config.getBUT_CALL_VOLUNTEER(), BUT_CALL_VOLUNTEER.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    public Keyboard takingPetMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_CARE_PET_REC(), BUT_CARE_PET_REC.getCallbackData()));
        buttons.add(new Button(config.getBUT_START_REGISTRATION(), BUT_START_REGISTRATION.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * метод формирует для сообщения активную кнопку ,<br>
     * загрузки отчета
     */
    public Keyboard fillOutReportActiveMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_FILL_OUT_REPORT_ON(), BUT_FILL_OUT_REPORT_ON.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * метод формирует для сообщения неактивную кнопку ,<br>
     * загрузки отчета
     */
    public Keyboard fillOutReportNotActiveMessageKeyboard() {
        log.info("Creating keyboard markup for taking pet message");
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(config.getBUT_FILL_OUT_REPORT_ON(), BUT_FILL_OUT_REPORT_OFF.getCallbackData()));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * функциональная клавиатура для информационного раздела приюта
     */
    public Keyboard shelterInformationFunctionalKeyboard() {
        log.info("Creating keyboard markup for shelters information windows");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_MORE_INFORMATION(), BUT_MORE_INFORMATION.getCallbackData()),
                new Button(config.getBUT_GO_TO_MAIN(), BUT_GO_TO_MAIN.getCallbackData()),
                new Button(config.getBUT_GO_TO_SHELTER_SELECT(), BUT_GO_TO_SHELTER_SELECT.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * клавиатура главного окна для информационного раздела приюта
     */
    public Keyboard shelterInformationMainKeyboard() {
        log.info("Creating keyboard markup for shelters information windows");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_GO_TO_MAIN(), BUT_GO_TO_MAIN.getCallbackData()),
                new Button(config.getBUT_GO_TO_SHELTER_SELECT(), BUT_GO_TO_SHELTER_SELECT.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);
    }

    /**
     * клавиатура для сообщения, где пользователя просят внести свои контактные данные
     */
    public Keyboard startRegistrationMessageKeyBoard() {
        log.info("Creating keyboard markup for set personal data of user");

        List<Button> buttons = new ArrayList<>(List.of(
                new Button(config.getBUT_SET_DATA_FROM_USER(), BUT_SET_DATA_FROM_USER.getCallbackData())
        ));
        return keyboardCreator.createInlineKeyboard(buttons);

    }

//    ...... клавиатуры для других типов сообщений.....

}
