package pro.sky.telegramBot.sender.specificSenders;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificDocumentMessageCreator;

import java.io.IOException;
import java.util.List;

/**
 * Методы класса подготавливают сообщение пользователю <br>
 * на присланный им документ и затем вызывают {@link #messageExecutor}, <br>
 * который выполняет отправку этого сообщения
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class DocumentMessageSender {
    private final UserService userService;
    private final SpecificDocumentMessageCreator specificDocumentMessageCreator;
    private final MessageExecutor messageExecutor;
    private final BotConfig botConfig;
    private final SpecificKeyboardCreator specificKeyboardCreator;

    /**
     * Метод формирует и отправляет сообщение пользователю о статусе полученного документа
     */
    public void sendReportResponseMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendReportResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createReportResponseMessage(chatId, document);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }
    /**
     * Метод формирует и отправляет сообщение после отправки им Exel документа c контактными данными
     */
    public void sendInfoTableResponseMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendInfoTableResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createInfoTableResponseMessage(chatId, document);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение после отправки им PDF файла со скринами его персональных документов
     */
    public void sendScreenPersonalDocumentsResponseMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendScreenPersonalDocumentsResponseMessage");
        try {
            User user = userService.findUserByChatId(chatId);
            List<User> users = userService.findAllByState(UserState.VOLUNTEER);

            SendPhoto sendPhoto;
            sendPhoto = specificDocumentMessageCreator.createScreenPersonalDocumentsResponseMessage(chatId, document);
            if (!users.isEmpty()) {
                String caption = String.format(botConfig.getMSG_SAVING_USER_PERSONAL_DOCS_SCREENS_SUCCESS(),
                        user.getShelter().getName());
                sendPhoto.caption(caption);
                sendPhoto.replyMarkup(specificKeyboardCreator.afterRegistrationFinalKeyboard());
                messageExecutor.executePhotoMessage(sendPhoto);
            }
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

}
