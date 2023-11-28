package pro.sky.telegramBot.executor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

// данный класс предназначен для выполнения отпрвки (execute) сообщений определенного содержания пользователю
@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class MessageExecutor {
    private final TelegramBot telegramBot;

    // метод выполняет отправку текстового сообщения пользователю с HTML разметкой
    public void executeHTMLMessage(SendMessage message) {
        log.info("Sending HTML text message to user");
        message.parseMode(HTML);
        telegramBot.execute(message);
    }

    // метод выполняет отправку пользователю сообщения с фотографией
    public void executePhotoMessage(SendPhoto sendPhoto) {
        log.info("Sending image message to user");
        telegramBot.execute(sendPhoto);
    }

    // метод выполняет отправку видео пользователю
    public void executeVideo(SendVideo sendVideoRequest) {
        log.info("Sending video message to user");
        telegramBot.execute(sendVideoRequest);
    }

    // метод выполняет отправку пользователю документа
    public void executeDocument(Long chatId, SendDocument sendDoc) {
        log.info("Sending document message to user");
        telegramBot.execute(sendDoc);
    }
}
