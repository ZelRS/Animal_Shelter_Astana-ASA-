package pro.sky.telegramBot.sender.specificSenders;

import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.mediaUtils.SpecificDocumentMessageCreator;

import static pro.sky.telegramBot.enums.UserState.PROBATION;
import static pro.sky.telegramBot.enums.UserState.PROBATION_PHOTO;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class PhotoMessageSender {

    private final SpecificDocumentMessageCreator specificDocumentMessageCreator;
    private final MessageExecutor messageExecutor;
    private final UserService userService;


    public void sendPhotoResponseMessage(PhotoSize[] photo, Long chatId) {
        log.info("Was invoked method sendPhotoResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createPhotoResponseMessage(chatId, photo);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

    public void sendPetPhotoForReportMessage(Long chatId) {
        log.info("Was invoked method sendPetPhotoForReportMessage");
        try {
            SendMessage sendMessage = new SendMessage(chatId, "Вложите фотографию");
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION)){
                user.setState(PROBATION_PHOTO);
                userService.update(user);
            }
            messageExecutor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.error("Failed to send photo response message to {}", chatId, e);
        }
    }
}
