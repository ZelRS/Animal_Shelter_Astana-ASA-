package pro.sky.telegramBot.handler.specificHandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class StartHandler {
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
        switch (user.getState()) {
            case FREE:
                messageSender.sendFirstTimeWelcomePhotoMessage(user.getUserName(), chatId);
//            case TRUSTED:
//                messageSender.sendChooseShelterMessage(chatId);
//                break;
            case POTENTIAL:
                messageSender.sendInfoForPotentialUserMessage(chatId);
                break;
//            case PROBATION:
//                messageSender.sendInfoForProbationUserMessage(chatId);
//                break;
            case UNTRUSTED:
                messageSender.sendSorryWelcomePhotoMessage(user.getUserName(), chatId);
                break;
            case BLOCKED:
                messageSender.sendBlockedWelcomePhotoMessage(user.getUserName(), chatId);
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

