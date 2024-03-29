package pro.sky.telegramBot.service.creators.mediaMessageCreators;

import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.MessageConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.ShelterService;

import javax.transaction.Transactional;
import java.io.IOException;

import static pro.sky.telegramBot.enums.MessageImage.*;
import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;

/**
 * методы класса собирают компоненты для конкретного сообщения, содержащего медиа-контент
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j  // SLF4J logging
public class SpecificMediaMessageCreator {
    private final GeneralMediaMessageCreator generalMediaMessageCreator;
    private final MessageConfig messageConfig;
    private final ShelterService shelterService;
    private final MediaMessageParams params = new MediaMessageParams();

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * который зашел в чат впервые
     */
    public SendPhoto createFirstTimeWelcomePhotoMessage(long chatId, String firstName) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(FIRST_TIME_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_SIMPLE_WELCOME(), firstName));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * имеющего статус UNTRUSTED("не надежный")
     */
    public SendPhoto createAnswerForUntrustedUserMessage(Long chatId, String firstName) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(SORRY_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_SORRY_WELCOME(), firstName));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * имеющего статус UNTRUSTED("не надежный")
     */
    public SendPhoto createInformationNotFoundMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(SORRY_WELCOME_MSG_IMG.getPath());
        params.setCaption("Упс! Простите. Информация не найдена в базе.");
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения приветствия для пользователя,<br>
     * имеющего статус BLOCKED("в черном списке")
     */
    public SendPhoto createBlockedWelcomePhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(BLOCKED_WELCOME_MSG_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_BLOCKED_WELCOME()));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения выбора приюта для собак
     */
    public SendPhoto createDogSheltersListPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(DOG_SHELTERS_MSG_IMG.getPath());
        int dogsCount = shelterService.findAllShelterNamesByType(DOG).size();
        if (dogsCount == 0) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_NULL(), DOG.getAccusative()));
        } else if (dogsCount == 1) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_ONE(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        } else if (dogsCount == 2 || dogsCount == 3) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_TWO(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        } else {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_THREE(), dogsCount, DOG.getAccusative()) + shelterService.getStringOfShelterNames(DOG));
        }
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения выбора приюта для кошек
     */
    public SendPhoto createCatSheltersListPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(CAT_SHELTERS_MSG_IMG.getPath());
        int catsCount = shelterService.findAllShelterNamesByType(CAT).size();
        if (catsCount == 0) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_NULL(), CAT.getAccusative()));
        } else if (catsCount == 1) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_ONE(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        } else if (catsCount == 2 || catsCount == 3) {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_TWO(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        } else {
            params.setCaption(String.format(messageConfig.getMSG_SHELTER_INTRO_THREE(), catsCount, CAT.getAccusative()) +
                    shelterService.getStringOfShelterNames(CAT));
        }
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения превью конкретного приюта,<br>
     */
    public SendPhoto createShelterFunctionalPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(SHELTER_DEFAULT_PREVIEW_MSG_IMG.getPath());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "выбрать животное"
     */
    public SendPhoto createTakingPetPhotoMessage(Long chatId, String firstName) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(TAKING_PET_MSG_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_TAKING_PET(), firstName));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "Рекомендации и советы" для собак
     */
    public SendPhoto createCareDogRecMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(CARE_DOG_REC_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_CARE_PET_REC() + messageConfig.getMSG_CARE_DOG_SPEC_REC());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "Рекомендации и советы" для кошек
     */
    public SendPhoto createCareCatRecMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(CARE_CAT_REC_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_CARE_PET_REC());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "отправить отчет"<br>
     * в интервале с 18:88 и до 21:00
     */
    public SendPhoto createReportSendTwoOptionsPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(TWO_OPTIONS_SEND_REPORT_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_SEND_REPORT_TWO_OPTIONS());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для фото-сообщения,<br>
     * когда пользователь нажал кнопку "отправить отчет"<br>
     * в интервале до 18:88 и после 21:00
     */
    public SendPhoto createReportSendOneOptionPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(ONE_OPTION_SEND_REPORT_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_SEND_REPORT_ONE_OPTION());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для отправки документа,<br>
     * когда пользователь выбрал команду /report
     */
    public SendDocument createReportSendDocumentMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath("/documents/report.xlsx");
        params.setFileName("report");
        return generalMediaMessageCreator.createDocumentMessage(params);
    }

    /**
     * сборка компонентов для отправки документа,<br>
     * когда пользователь нажал на ссылку у конкретного документа
     */
    public SendDocument createRecDocDocumentMessage(Integer refNum, Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath("/documents/tips_and_tricks/" + refNum + "rec.txt");
        params.setFileName(refNum + "rec");
        return generalMediaMessageCreator.createTXTDocumentMessage(params);
    }

    /**
     * сборка компонентов для отправки документа,<br>
     * когда пользователь выбрал команду /info_table
     */
    public SendDocument createInfoTableDocumentMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath("/documents/info_table.xlsx");
        params.setFileName("info_table");
        return generalMediaMessageCreator.createInfoTableXLSXDocumentMessage(params);
    }

    //Метод предоставляет данные для вопроса отчета
    public SendPhoto createQuestionForReportMessage(Long chatId, String question) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(QUESTION_FOR_REPORT_IMG.getPath());
        params.setCaption(question);
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    //Метод предоставляет данные для сообщения пользователю. что его отчет принят
    public SendPhoto createReportAcceptedPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(REPORT_ACCEPTED_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_REPORT_ACCEPTED());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createVolunteerWelcomePhotoMessage(Long chatId, String firstName) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(HELLO_VOLUNTEER_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_HELLO_VOLUNTEER(), firstName));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createChooseShelterPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(CHOOSE_SHELTER_IMG.getPath());
        params.setCaption(messageConfig.getMSG_CHOOSE_SHELTER());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * сборка компонентов для отправки документа,<br>
     * когда пользователь нажал на кнопку "позвать волонтёра"
     */
    public SendPhoto createCallVolunteerPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(CALL_VOLUNTEER_MSG_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_VOLUNTEER_NOTIFIED()));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * Метод передает параметры для сообщения о возможности онлайн заполнения отчета
     */
    public SendPhoto createNotificationToAdopterAboutDailyReportPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * Метод передает параметры для сообщения о начале процедуры онлайн заполнения отчета
     */
    public SendPhoto createNotificationToAdopterAboutStartReportPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ABOUT_START_REPORTING());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * Метод передает параметры для сообщения о завершении процедуры онлайн заполнения отчета
     */
    public SendPhoto createNotificationToAdopterAboutEndReportPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ABOUT_END_REPORTING());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createMissingPetMessageToVolunteerPhotoMessage(Long volunteerChatId, Long userId) throws IOException {
        params.setChatId(volunteerChatId);
        params.setFilePath(MISSING_PET_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_MISSING_PET(), userId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createAskVolunteerForHelpPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(REPORT_NOT_ACCEPTED_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_GET_HELP_FROM_VOLUNTEER());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNeedToSendReportPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NEED_TO_SEND_REPORT());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNeedToSendPhotoForReportPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NEED_TO_SEND_PHOTO_FOR_REPORT());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToVolunteerAboutProblemPhotoMessage(Long chatId, Long userChatId)
            throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_PROBLEM_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_PROBLEM(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToVolunteerAboutTryYourBest(Long chatId, Long userChatId)
            throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TRY_YOUR_BEST_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_TRY_YOUR_BEST(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToVolunteerAboutGoodJob(Long chatId, Long userChatId)
            throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_GOOD_JOB_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_GOOD_JOB(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToVolunteerAboutCalculationsError(Long chatId, Long userChatId)
            throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_CALCULATION_ERROR_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_CALCULATION_ERROR(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToAdopterAboutProblemPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_PROBLEM_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_ABOUT_PROBLEM());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToAdopterAboutTryYourBest(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TRY_YOUR_BEST_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_ABOUT_TRY_YOUR_BEST());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToAdopterAboutGoodJob(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_GOOD_JOB_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_ABOUT_GOOD_JOB());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createUnsuccessfulNotificationToVolunteerPhotoMessage(Long chatId, Long userChatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_PROBLEM_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_FAILED(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToVolunteerAboutExtension(Long chatId, Long userChatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TRY_YOUR_BEST_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_EXTENSION(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createSuccessfulNotificationToVolunteer(Long chatId, Long userChatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_GOOD_JOB_IMG.getPath());
        params.setCaption(String.format(messageConfig.getMSG_NOTIFICATION_SUCCESSFUL(), userChatId));
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createUnsuccessfulNotificationToAdopterPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_PROBLEM_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_FAILED());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createNotificationToAdopterAboutExtension(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_TRY_YOUR_BEST_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_EXTENSION());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createSuccessfulNotificationToAdopter(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(NOTIFICATION_GOOD_JOB_IMG.getPath());
        params.setCaption(messageConfig.getMSG_NOTIFICATION_ADOPTER_SUCCESSFUL());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createShelterFullInfoPhotoMessage(Long chatId) throws IOException {
        params.setChatId(chatId);
        params.setFilePath(SHELTER_INFORMATION_MSG_IMG.getPath());
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

//    .......  медиа-сообщения для других целей......


}
