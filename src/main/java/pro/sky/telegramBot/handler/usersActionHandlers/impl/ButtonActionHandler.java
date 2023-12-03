package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.usersActionHandlers.ActionHandler;
import pro.sky.telegramBot.sender.MessageSender;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.CallbackData.*;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при его нажатии на определенную кнопку
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class ButtonActionHandler implements ActionHandler {
    private final MessageSender messageSender;

    @FunctionalInterface
    interface Button {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, Button> buttonMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #buttonMap} с кнопками,<br>
     * при нажатии которых должен высылаться конкретный ответ
     */
    @PostConstruct
    public void init() {

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА РОМАНА
        buttonMap.put(BUT_TAKING_PET.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_TAKING_PET button");
            messageSender.sendTakingPetPhotoMessage(chatId, firstName);
        });
// Роман
        buttonMap.put(BUT_CARE_PET_REC.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_CARE_PET_RECOMMENDATIONS button");
            messageSender.sendCarePetRecMessage(chatId);
        });

// Роман
        buttonMap.put(BUT_START_REGISTRATION.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_START_REGISTRATION button");
            messageSender.sendStartRegistrationMessage(chatId, firstName);
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА ЮРИЯ ПЕТУХОВА
        buttonMap.put(BUT_SEND_REPORT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed SEND_REPORT button");
            messageSender.sendReportPhotoMessage(chatId);
        });
        buttonMap.put(BUT_FILL_OUT_REPORT_ON.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed FILL_OUT_REPORT button");
//            messageSender.sendReportFillOutMessage(chatId);
        });
        buttonMap.put(BUT_FILL_OUT_REPORT_OFF.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed FILL_OUT_REPORT button");
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА АЛЕКСЕЯ
        buttonMap.put(BUT_CALL_VOLUNTEER.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed CALL_VOLUNTEER button");

            // внизу заглушка! там должен быть метод, который будет высылаиь ответ при нажатии кнопки "позвать волонтера"
            // на кнопки пока не нажимайте. прилложение завалится. этот метод закомментирован и не подходит под реализуцию,
            // пожалуйста не вводите его из комментов. реализуйте сюда свой send по вашему заданию

            // messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

//точка входа в информационное меню приюта
        buttonMap.put(BUT_GET_FULL_INFO.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed GET_FULL_INFO button");
            messageSender.sendShelterFullInfoHTMLMessage(firstName, lastName, chatId);
        });
        buttonMap.put(BUT_WANT_DOG.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed WANT_DOG button");
            messageSender.sendDogSheltersListPhotoMessage(chatId);
        });
        buttonMap.put(BUT_WANT_CAT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed WANT_CAT button");
            messageSender.sendCatSheltersListPhotoMessage(chatId);
        });

//возврат с навигационного меню в меню информации о приюте
        buttonMap.put(BUT_MORE_INFORMATION.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_MORE_INFORMATION button");
            messageSender.sendShelterFullInfoHTMLMessage(firstName, lastName, chatId);
        });

//Возврат в главное меню с навигационных кнопок
        buttonMap.put(BUT_GO_TO_MAIN.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_GO_TO_MAIN button");
            messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

//возврат к выбору приюта c навигационных кнопок
        buttonMap.put(BUT_GO_TO_SHELTER_SELECT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_GO_TO_SHELTER_SELECT button");
            messageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId);
        });
    }

    /**
     * Метод ищет, есть ли в {@link #buttonMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String callbackData, String firstName, String lastName, Long chatId) {
        Button commandToRun = buttonMap.get(callbackData.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for button: {}", callbackData);
            // отправка дефолтного сообщения
            messageSender.sendDefaultHTMLMessage(chatId);
        }
    }
}
