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
    if (user != null) {
        switch (user.getState()) {
            case IDLE:
                log.info("Received START command from a new saved user");
                messageHandler.sendChooseShelterMessage(chatId);
                break;
            case POTENTIAL:
                log.info("Received START command from a potential user");
                messageHandler.sendInfoForPotentialUserMessage(chatId);
                break;
            case PROBATION:
                log.info("Received START command from a probation user");
                messageHandler.sendInfoForProbationUserMessage(chatId);
                break;
            case TRUSTED:
                log.info("Received START command from a trusted user");
                messageHandler.sendChooseShelterMessage(chatId);
                break;
            case UNTRUSTED:
                log.info("Received START command from a untrusted user");
                messageHandler.sendSorryMessage(chatId);
                break;
            case BLOCKED:
                log.info("Received START command from a blocked user");
                messageHandler.sendBlockedMessage(chatId);
                break;
        }
    } else {
        try {
            log.info("Received START command from a first time user");
            messageHandler.sendWelcomeMessage(firstName, chatId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    }
}
