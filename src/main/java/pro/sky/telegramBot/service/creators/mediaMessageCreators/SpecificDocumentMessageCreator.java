package pro.sky.telegramBot.service.creators.mediaMessageCreators;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.MessageConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.loaders.DocumentLoader;
import pro.sky.telegramBot.service.loaders.MediaLoader;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.ReportService;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import java.io.IOException;
import java.util.List;

import static pro.sky.telegramBot.enums.MessageImage.*;

/**
 * Класс сбора параметров для сообщения пользователю при получении документа
 */

@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificDocumentMessageCreator {

    private final DocumentLoader documentLoader;
    private final MediaLoader mediaLoader;
    private final ReportService reportService;
    private final MessageConfig messageConfig;
    private final GeneralMediaMessageCreator generalMediaMessageCreator;
    private final UserService userService;

    public SendPhoto createReportResponseMessage(Long chatId, Document document) throws IOException {

        //Получаем значения из отчета
        List<String> values = documentLoader.readAdopterReport(document);

        MediaMessageParams params = new MediaMessageParams();
        if (reportService.createReportFromExcel(chatId, values)) {
            params.setChatId(chatId);
            params.setFilePath(REPORT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(messageConfig.getMSG_REPORT_ACCEPTED());
        } else {
            params.setChatId(chatId);
            params.setFilePath(REPORT_NOT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(messageConfig.getMSG_REPORT_NOT_ACCEPTED());
        }
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    public SendPhoto createPhotoResponseMessage(Long chatId, PhotoSize[] photo) throws IOException {
        log.info("Was invoked createPhotoResponseMessage method for {}", chatId);
        MediaMessageParams params = new MediaMessageParams();
        if (reportService.attachPhotoToReport(chatId, photo)) {
            log.info("Parameter were set for {}", chatId);
            params.setChatId(chatId);
            params.setFilePath(REPORT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(messageConfig.getMSG_PHOTO_ACCEPTED());
        } else {
            params.setChatId(chatId);
            params.setFilePath(REPORT_NOT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(messageConfig.getMSG_PHOTO_NOT_ACCEPTED());
        }
        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * метод принимает список значений внесенных пользователем данных в таблицу
     * сохраняет их в таблицу Person_Info и присваивает конкретному пользователю в таблице Person,
     * а затем отправляет пользователю сообщение о том, что данные успешно сохранены и указывает его дальнейшие действия
     */
    public SendPhoto createInfoTableResponseMessage(Long chatId, Document document) throws IOException {
        //Получаем значения из отчета
        List<String> values = documentLoader.readInfoTable(document);
        User user = userService.findUserByChatId(chatId);

        UserInfo userInfo = new UserInfo();

        userInfo.setFirstName(values.get(0));
        userInfo.setLastName(values.get(1));
        userInfo.setAddress(values.get(2));
        userInfo.setPassport(values.get(3));
        userInfo.setPhone(values.get(4));
        userInfo.setEmail(values.get(5));
        userService.createUserInfo(userInfo);
        user.setUserInfo(userInfo);
        user.setState(UserState.POTENTIAL);


        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(SAVING_USER_INFO_SUCCESS_MSG_IMG.getPath());
        params.setCaption(messageConfig.getMSG_SAVING_USER_INFO_SUCCESS());

        return generalMediaMessageCreator.createPhotoMessage(params);
    }

    /**
     * метод принимает от пользователя PDF файл со скринами личных документов, c помощью documentLoader читает и
     * отправляет его всем волонтерам,
     * а затем отправляет пользователю сообщение о том, что все необходимые условия оформления выполнены
     * и он приглашается с личной явкой в приют, для выбора животного
     */
    public SendPhoto createScreenPersonalDocumentsResponseMessage(Long chatId, Document document) throws IOException {
        documentLoader.readAndSendScreenPersonalDocumentsToVolunteers(document, chatId);

        MediaMessageParams params = new MediaMessageParams();
        params.setChatId(chatId);
        params.setFilePath(SAVING_USER_PERSONAL_DOCS_SCREENS_MSG_IMG.getPath());

        User user = userService.findUserByChatId(chatId);
        if (!user.getState().equals(UserState.VOLUNTEER)) {
            user.setState(UserState.INVITED);
        }

        return generalMediaMessageCreator.createPhotoMessage(params);
    }

}
