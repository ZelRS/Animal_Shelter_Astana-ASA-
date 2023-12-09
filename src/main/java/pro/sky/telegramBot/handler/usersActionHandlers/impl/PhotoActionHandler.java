package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.sender.specificSenders.PhotoMessageSender;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.UserState.BLOCKED;
import static pro.sky.telegramBot.enums.UserState.PROBATION;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class PhotoActionHandler {
    private final UserService userService;
    private final MessageSender messageSender;
    private final BlockedUserHandler blockedUserHandler;
    private final PhotoMessageSender photoMessageSender;

    @FunctionalInterface
    interface PhotoProcessor {
        void processPhoto(PhotoSize[] photo, Long chatId);
    }

    private final Map<UserState, PhotoActionHandler.PhotoProcessor> photoMap = new HashMap<>();

    @PostConstruct
    public void init() {

        photoMap.put(PROBATION, (photo, chatId) -> {
            log.info("Processing pet.jpg photo");
            // Проверяем, есть ли пользователь и может ли он присылать фото
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION) && user.getAdoptionRecord() != null) {
                photoMessageSender.sendPhotoResponseMessage(photo, chatId);
            } else {
                //Сообщаем, что отсутствует запись об усыновлении
                messageSender.sendNoAdoptionRecordMessage(chatId);
            }
        });
    }

    public void handle(PhotoSize[] photo, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getState().equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
        }
        if (user != null && user.getState().equals(PROBATION)) {
            PhotoActionHandler.PhotoProcessor photoProcessor = photoMap.get(PROBATION);
            if (photoProcessor != null) {
                photoProcessor.processPhoto(photo, chatId);
            } else {
                // Если не нашли обработчик для присланной фотографии
                log.warn("No handler found for the state");
            }
        }
    }
}
