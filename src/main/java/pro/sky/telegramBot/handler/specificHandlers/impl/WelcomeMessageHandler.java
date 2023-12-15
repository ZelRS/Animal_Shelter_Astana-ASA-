package pro.sky.telegramBot.handler.specificHandlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * класс для обработки стартового приветственного сообщения
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class WelcomeMessageHandler {
    private final UserService userService;

    private final pro.sky.telegramBot.sender.MessageSender messageSender;

    public void handleStartCommand(String firstName, Long chatId) {
        User user = userService.findUserByChatId(chatId);

        if (user == null) {
            log.info("Received START command from a first-time user");
            sendMessageWithExceptionHandling(() -> messageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId));
            user = new User();
            user.setChatId(chatId);
            user.setUserName(firstName);
            user.setState(UserState.FREE);
            userService.create(user);
            return;
        }
        log.info("Received START command from user in state: {}", user.getState());
        sendUserStateSpecificMessage(user, chatId);
    }
    private void sendMessageWithExceptionHandling(MessageSender messageSender) {
        try {
            messageSender.send();
        } catch (IOException e) {
            log.error("An error occurred while sending a message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private void sendUserStateSpecificMessage(User user, Long chatId) {
        Map<UserState, MessageSender> stateMessageMap = new HashMap<>();
        stateMessageMap.put(UserState.FREE, () -> messageSender.sendFirstTimeWelcomePhotoMessage(user.getUserName(), chatId));
        stateMessageMap.put(UserState.POTENTIAL, () -> messageSender.sendChooseShelterMessage(chatId));
        stateMessageMap.put(UserState.INVITED, () -> messageSender.sendFirstTimeWelcomePhotoMessage(user.getUserName(), chatId));
        stateMessageMap.put(UserState.PROBATION, () -> messageSender.sendReportPhotoMessage(chatId));
        stateMessageMap.put(UserState.VOLUNTEER, () -> messageSender.sendVolunteerWelcomePhotoMessage(user.getUserName(), chatId));
        stateMessageMap.put(UserState.UNTRUSTED, () -> messageSender.sendFirstTimeWelcomePhotoMessage(user.getUserName(), chatId));

        MessageSender defaultMessageSender = () -> {
            log.warn("Unknown user state: {}", user.getState());
            messageSender.sendDefaultHTMLMessage(chatId);
        };

        MessageSender messageSenderAction = stateMessageMap.getOrDefault(user.getState(), defaultMessageSender);
        sendMessageWithExceptionHandling(messageSenderAction);
    }

    @FunctionalInterface
    private interface MessageSender {
        void send() throws IOException;
    }
}

