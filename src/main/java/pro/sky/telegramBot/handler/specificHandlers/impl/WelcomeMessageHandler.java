package pro.sky.telegramBot.handler.specificHandlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.UserState.*;

/**
 * класс для обработки стартового приветственного сообщения
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class WelcomeMessageHandler {
    private final pro.sky.telegramBot.sender.MessageSender messageSender;

    public void handleStartCommand(String firstName, Long chatId, UserState userState) {
        log.info("Received START command from user in state: {}", userState);
        sendUserStateSpecificMessage(firstName, chatId, userState);
    }

    private void sendUserStateSpecificMessage(String firstName, Long chatId, UserState userState) {
        Map<UserState, MessageSender> stateMessageMap = new HashMap<>();
        stateMessageMap.put(FREE, () -> messageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId));
        stateMessageMap.put(POTENTIAL, () -> messageSender.sendChooseShelterMessage(chatId));
        stateMessageMap.put(INVITED, () -> messageSender.sendShelterFunctionalPhotoMessage(chatId));
        stateMessageMap.put(PROBATION, () -> messageSender.sendReportPhotoMessage(chatId));
        stateMessageMap.put(VOLUNTEER, () -> messageSender.sendVolunteerWelcomePhotoMessage(firstName, chatId));
        stateMessageMap.put(UNTRUSTED, () -> messageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId));

        MessageSender defaultMessageSender = () -> {
            log.warn("Unknown user state: {}", userState);
            messageSender.sendDefaultHTMLMessage(chatId);
        };

        MessageSender messageSenderAction = stateMessageMap.getOrDefault(userState, defaultMessageSender);
        sendMessageWithExceptionHandling(messageSenderAction);
    }

    private void sendMessageWithExceptionHandling(MessageSender messageSender) {
        try {
            messageSender.send();
        } catch (IOException e) {
            log.error("An error occurred while sending a message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    private interface MessageSender {
        void send() throws IOException;
    }
}

