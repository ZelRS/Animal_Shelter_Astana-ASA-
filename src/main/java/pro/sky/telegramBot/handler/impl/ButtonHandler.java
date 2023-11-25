package pro.sky.telegramBot.handler.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.Handler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.CallbackData.CAT_BUT;
import static pro.sky.telegramBot.enums.CallbackData.DOG_BUT;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class ButtonHandler implements Handler {
    private final MessageHandlerImpl messageHandler;

    @FunctionalInterface
    interface Button {
        void run(String firstName, String lastName, Long chatId);
    }

    Map<String, Button> buttonMap = new HashMap<>();

    @PostConstruct
    public void init() {

        buttonMap.put(DOG_BUT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed DOG button");
            messageHandler.sendDogsButMessage(chatId);
        });
        buttonMap.put(CAT_BUT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed CAT button");
            messageHandler.sendCatsButMessage(chatId);
        });
    }
    @Override
    public void handle(String callbackData, String firstName, String lastName, Long chatId) {
        Button commandToRun = buttonMap.get(callbackData.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for button: {}", callbackData);
            messageHandler.sendDefaultMessage(chatId);
        }
    }
}
