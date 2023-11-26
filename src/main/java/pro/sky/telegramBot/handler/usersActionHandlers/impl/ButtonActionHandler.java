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

import static pro.sky.telegramBot.enums.CallbackData.CAT_BUT;
import static pro.sky.telegramBot.enums.CallbackData.DOG_BUT;

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
        buttonMap.put(DOG_BUT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed DOG button");
            messageSender.sendDogSheltersListPhotoMessage(chatId);
        });
        buttonMap.put(CAT_BUT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed CAT button");
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
            messageSender.sendDefaultMessage(chatId);
        }
    }
}
