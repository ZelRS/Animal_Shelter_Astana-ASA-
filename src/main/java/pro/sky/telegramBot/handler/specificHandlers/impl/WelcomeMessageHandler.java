package pro.sky.telegramBot.handler.specificHandlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.sender.specificSenders.HTMLMessageSender;
import pro.sky.telegramBot.sender.specificSenders.PhotoMessageSender;

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
    private final HTMLMessageSender HTMLMessageSender;
    private final PhotoMessageSender photoMessageSender;

    public void handleStartCommand(String firstName, Long chatId, UserState userState) {
        log.info("Received START command from user in state: {}", userState);
        sendUserStateSpecificMessage(firstName, chatId, userState);
    }

    private void sendUserStateSpecificMessage(String firstName, Long chatId, UserState userState) {
        Map<UserState, MessageSender> stateMessageMap = new HashMap<>();
        stateMessageMap.put(FREE, () -> photoMessageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId));
        stateMessageMap.put(POTENTIAL, () -> photoMessageSender.sendChooseShelterPhotoMessage(chatId));
        stateMessageMap.put(INVITED, () -> photoMessageSender.sendShelterFunctionalPhotoMessage(chatId));
        stateMessageMap.put(PROBATION, () -> photoMessageSender.sendReportPhotoMessage(chatId));
        stateMessageMap.put(VOLUNTEER, () -> photoMessageSender.sendVolunteerWelcomePhotoMessage(firstName, chatId));
        stateMessageMap.put(UNTRUSTED, () -> photoMessageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId));

        MessageSender defaultMessageSender = () -> {
            log.warn("Unknown user state: {}", userState);
            HTMLMessageSender.sendDefaultHTMLMessage(chatId);
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

