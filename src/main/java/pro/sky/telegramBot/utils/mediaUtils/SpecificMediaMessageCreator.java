package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.service.ShelterService;

import java.io.IOException;

import static pro.sky.telegramBot.enums.ImageNames.*;
import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;

// класс содержит функционал, закрепляюищий определенный медиа-контент за конкретнымсообщением
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificMediaMessageCreator {
    private final MediaMessageCreator mediaMessageCreator;
    private final BotConfig config;
    private final ShelterService shelterService;

    // метод закрепляет конкретное фото за приветственным сообщением
    public SendPhoto createWelcomeMessagePhoto(long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(WELCOME_IMG.getPath());
        params.setCaption(String.format(config.getWELCOME_MES(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод закрепляет конкретное фото за сообщением со списком приютов для собак
    public SendPhoto createDogSheltersListMessagePhoto(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(DOGS_IMG.getPath());
        params.setCaption(String.format(config.getSHELTER_INTRO_MES()) + shelterService.getShelterNames(DOG));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    // метод закрепляет конкретное фото за сообщением со списком приютов для собак
    public SendPhoto createCatSheltersListMessagePhoto(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CATS_IMG.getPath());
        params.setCaption(String.format(config.getSHELTER_INTRO_MES()) + shelterService.getShelterNames(CAT));
        return mediaMessageCreator.createPhotoMessage(params);
    }

//    ....... закрепление конкретного медиа-контента за определенным сообщением......
}
