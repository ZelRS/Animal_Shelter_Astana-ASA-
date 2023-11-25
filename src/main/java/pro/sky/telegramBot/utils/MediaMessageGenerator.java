package pro.sky.telegramBot.utils;

import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
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

@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class MediaMessageGenerator {

    private final MediaLoader mediaLoader;
    private final BotConfig config;
    private final ShelterService shelterService;

    public SendPhoto createPhotoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.imageCreator(params.getChatId(), params.getFilePath(), params.getCaption());
    }

    public SendVideo createVideoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.videoCreator(params.getChatId(), params.getFilePath(), params.getFileName());
    }

    public SendDocument createDocumentMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.documentCreator(params.getChatId(), params.getFilePath(), params.getFileName());
    }

    public SendPhoto welcomeMessagePhotoCreate(long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(WELCOME_IMG.getPath());
        params.setCaption(String.format(config.getWELCOME_MES(), firstName));
        return createPhotoMessage(params);
    }

    public SendPhoto dogsButMessagePhotoCreate(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(DOGS_IMG.getPath());
        params.setCaption(String.format(config.getSHELTER_INTRO_MES()) + shelterService.getShelterNames(DOG));
        return createPhotoMessage(params);
    }

    public SendPhoto catsButMessagePhotoCreate(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CATS_IMG.getPath());
        params.setCaption(String.format(config.getSHELTER_INTRO_MES()) + shelterService.getShelterNames(CAT));
        return createPhotoMessage(params);
    }
}
