package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.service.ShelterService;

import java.io.IOException;

import static pro.sky.telegramBot.enums.MessageImage.*;
import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;

// класс содержит функционал, закрепляюищий конкретный медиа-контент за конкретным сообщением
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificMediaMessageCreator {
    private final MediaMessageCreator mediaMessageCreator;
    private final BotConfig config;
    private final ShelterService shelterService;

    // метод создает фото-сообщение приветствия для пользователя, который зашел в чат впервые
    public SendPhoto createFirstTimeWelcomePhotoMessage(long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(FIRST_TIME_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SIMPLE_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод создает фото-сообщение приветствия для пользователя, который имеющего стутус UNTRUSTED("не надежный")
    public SendPhoto createSorryWelcomePhotoMessage(Long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(SORRY_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SORRY_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод создает фото-сообщение приветствия для пользователя, который имеющего стутус BLOCKED("в черном списке")
    public SendPhoto createBlockedWelcomePhotoMessage(Long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(BLOCKED_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_BLOCKED_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод создает фото-сообщение выбора приюта для собак
    public SendPhoto createDogSheltersListPhotoMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(DOG_SHELTERS_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SHELTER_INTRO()) + shelterService.getShelterNames(DOG));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод создает фото-сообщение выбора приюта для кошек
    public SendPhoto createCatSheltersListPhotoMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CAT_SHELTERS_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SHELTER_INTRO()) + shelterService.getShelterNames(CAT));
        return mediaMessageCreator.createPhotoMessage(params);
    }

//    ....... создает фото-сообщения для других целей......
}
