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

    // при запуске приложения происходит наполнение мапы с кнопками, при нажатии которых должен высылаться конкретный ответ
    @PostConstruct
    public void init() {

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА РОМАНА
        buttonMap.put(BUT_WANT_TAKE_PET.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed WANT_TAKE_PET button");

            // внизу заглушка! там должен быть метод, который будет высылаиь ответ при нажатии кнопки "взять животное"
            // на кнопки пока не нажимайте. прилложение завалится. этот метод закомментирован и не подходит под реализуцию,
            // пожалуйста не вводите его из комментов. реализуйте сюда свой send по вашему заданию

//            messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА ЮРИЯ ПЕТУХОВА
        buttonMap.put(BUT_SEND_REPORT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed SEND_REPORT button");

            // внизу заглушка! там должен быть метод, который будет высылать ответ при нажатии кнопки "отправить отчет"
            // на кнопки пока не нажимайте. прилложение завалится. этот метод закомментирован и не подходит под реализуцию,
            // пожалуйста не вводите его из комментов. реализуйте сюда свой send по вашему заданию

//            messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА АЛЕКСЕЯ
        buttonMap.put(BUT_CALL_VOLUNTEER.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed CALL_VOLUNTEER button");

            // внизу заглушка! там должен быть метод, который будет высылаиь ответ при нажатии кнопки "позвать волонтера"
            // на кнопки пока не нажимайте. прилложение завалится. этот метод закомментирован и не подходит под реализуцию,
            // пожалуйста не вводите его из комментов. реализуйте сюда свой send по вашему заданию

            // messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА ЮРИЯ ЯЦЕНКО
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
    }

    // метод ищет, есть ли в мапе кнопка по ключу.
    // Если кнопка есть, совершает логику, лежащую в значении по этому ключу.
    // Если такой команды в мапе нет, отправляет дефолтное сообщение
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
