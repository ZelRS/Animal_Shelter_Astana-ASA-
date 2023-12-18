package pro.sky.telegramBot.service.handlers.usersActionHandlers.impl;

import com.pengrad.telegrambot.model.PhotoSize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.handlers.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.service.handlers.usersActionHandlers.PhotoActionHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.senders.HTMLMessageSender;
import pro.sky.telegramBot.service.senders.PhotoMessageSender;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.UserState.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class PhotoActionHandlerImpl implements PhotoActionHandler {
    private final UserService userService;
    private final HTMLMessageSender HTMLMessageSender;
    private final BlockedUserHandler blockedUserHandler;
    private final PhotoMessageSender photoMessageSender;

    @FunctionalInterface
    interface PhotoProcessor {
        void processPhoto(PhotoSize[] photo, Long chatId);
    }

    private final Map<UserState, PhotoActionHandlerImpl.PhotoProcessor> photoMap = new HashMap<>();

    @PostConstruct
    public void init() {

        photoMap.put(PROBATION_PHOTO, (photo, chatId) -> {
            log.info("Processing pet.jpg photo");
            // Проверяем, есть ли пользователь и может ли он присылать фото
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION_PHOTO) && user.getAdoptionRecord() != null) {
                photoMessageSender.sendPhotoResponsePhotoMessage(photo, chatId);
            } else {
                //Сообщаем, что отсутствует запись об усыновлении
                HTMLMessageSender.sendNoAdoptionRecordHTMLMessage(chatId);
            }
        });
    }

    @Override
    public void handle(PhotoSize[] photo, Long chatId, UserState userState) {
        if (userState.equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
            return;
        }
        PhotoActionHandlerImpl.PhotoProcessor photoProcessor = photoMap.get(userState);
        if (photoProcessor != null) {
            photoProcessor.processPhoto(photo, chatId);
        } else {
            // Если не нашли обработчик для присланной фотографии
            log.warn("No handler found for the state");
        }
    }
}
