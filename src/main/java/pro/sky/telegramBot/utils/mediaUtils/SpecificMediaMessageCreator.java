package pro.sky.telegramBot.utils.mediaUtils;

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

/**
 * методы класса собирают компоненты для конкретного сообщения, содержащего медиа-контент
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificMediaMessageCreator {
    private final MediaMessageCreator mediaMessageCreator;
    private final BotConfig config;
    private final ShelterService shelterService;

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * который зашел в чат впервые
     */
    public SendPhoto createFirstTimeWelcomePhotoMessage(long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(FIRST_TIME_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SIMPLE_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * имеющего статус UNTRUSTED("не надежный")
     */
    public SendPhoto createSorryWelcomePhotoMessage(Long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(SORRY_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_SORRY_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * имеющего статус BLOCKED("в черном списке")
     */
    public SendPhoto createBlockedWelcomePhotoMessage(Long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(BLOCKED_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_BLOCKED_WELCOME(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения выбора приюта для собак
     */
    public SendPhoto createDogSheltersListPhotoMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(DOG_SHELTERS_MSG_IMG.getPath());
        int dogsCount = shelterService.findAllShelterNamesByType(DOG).size();
        if (dogsCount == 0) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_NULL(), DOG.getAccusative()));
        } else if (dogsCount == 1) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_ONE(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        } else if (dogsCount == 2 || dogsCount == 3) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_TWO(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        } else {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_THREE(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        }
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения выбора приюта для кошек
     */
    public SendPhoto createCatSheltersListPhotoMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CAT_SHELTERS_MSG_IMG.getPath());
        int catsCount = shelterService.findAllShelterNamesByType(CAT).size();
        if (catsCount == 0) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_NULL(), CAT.getAccusative()));
        } else if (catsCount == 1) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_ONE(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        } else if (catsCount == 2 || catsCount == 3) {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_TWO(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        } else {
            params.setCaption(String.format(config.getMSG_SHELTER_INTRO_THREE(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        }
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения превью конкретного приюта,<br>
     */
    public SendPhoto createShelterFunctionalPhotoMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(SHELTER_DEFAULT_PREVIEW_MSG_IMG.getPath());
        return mediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "выбрать животное"
     */
    public SendPhoto createTakingPetPhotoMessage(Long chatId, String firstName) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(TAKING_PET_MSG_IMG.getPath());
        params.setCaption(String.format(config.getMSG_TAKING_PET(), firstName));
        return mediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createCareDogRecMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CARE_DOG_REC_MSG_IMG.getPath());
        params.setCaption(config.getMSG_CARE_PET_REC() + config.getMSG_CARE_DOG_SPEC_REC());
        return mediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createCareCatRecMessage(Long chatId) throws IOException {
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(CARE_CAT_REC_MSG_IMG.getPath());
        params.setCaption(config.getMSG_CARE_PET_REC());
        return mediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createDownloadMessege(Long chatId) throws IOException{
        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(DOWNLOAD_REPORT_MESSEGE_IMG.getPath());
        params.setCaption(config.getMSG_DOWNLOAD_REPORT_MESSEGE());
        return mediaMessageCreator.createPhotoMessage(params);
    }


//    .......  фото-сообщения для других целей......


}
