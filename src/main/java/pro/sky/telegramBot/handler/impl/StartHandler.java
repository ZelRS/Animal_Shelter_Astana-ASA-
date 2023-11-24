package pro.sky.telegramBot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.MessageHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class StartHandler {

    private final UserService userService;
    private final MessageHandler messageHandler;

    public void handleStartCommand(String firstName, Long chatId) {
        User user = userService.findUserByChatId(chatId);

        if (user == null) {
            log.info("Received START command from a first-time user");
            sendMessageWithExceptionHandling(() -> messageHandler.sendWelcomeMessage(firstName, chatId));
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
        switch (user.getState()) {
            case IDLE:
            case TRUSTED:
                messageHandler.sendChooseShelterMessage(chatId);
                break;
            case POTENTIAL:
                messageHandler.sendInfoForPotentialUserMessage(chatId);
                break;
            case PROBATION:
                messageHandler.sendInfoForProbationUserMessage(chatId);
                break;
            case UNTRUSTED:
                messageHandler.sendSorryMessage(chatId);
                break;
            case BLOCKED:
                messageHandler.sendBlockedMessage(chatId);
                break;
            default:
                log.warn("Unknown user state: {}", user.getState());
                break;
        }
    }
    @FunctionalInterface
    private interface MessageSender {
        void send() throws IOException;
    }
}

