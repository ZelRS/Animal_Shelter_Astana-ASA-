package pro.sky.telegramBot.sender.specificSenders;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificDocumentMessageCreator;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class PhotoMessageSender {

    private final SpecificDocumentMessageCreator specificDocumentMessageCreator;
    private final MessageExecutor messageExecutor;


    public void sendPhotoResponseMessage(PhotoSize[] photo, Long chatId) {
        log.info("Was invoked method sendPhotoResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createPhotoResponseMessage(chatId, photo);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }
}
