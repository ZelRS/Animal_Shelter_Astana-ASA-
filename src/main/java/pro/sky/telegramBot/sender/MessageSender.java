package pro.sky.telegramBot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

// данный класс предназначен для выполнения отпрвки (execute) сообщений определенного содержания пользователю
@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class MessageSender {
    private final TelegramBot telegramBot;
    // мапа для текста, который не вошел в сообщение и который будет отправлен позже, если пользователь нажмет продолжить
    // (пока не используется)
    private final Map<Long, String> remainingMessages = new ConcurrentHashMap<>();

    // метод выполняет отравку простого текстового сообщения пользователю
    public void executeMessage(SendMessage message) {
        log.info("Message executing to user");
        telegramBot.execute(message);
    }

    // метод выполняет отравку текстового сообщения пользователю с HTML разметкой
    public void sendHTMLMessage(long chatId, String messageText) {
        log.info("Sending HTML text message to user");
        SendMessage message = new SendMessage(String.valueOf(chatId), messageText).parseMode(HTML);
        telegramBot.execute(message);
    }

    // метод выполняет отравку фото пользователю
    public void sendImageMessage(SendPhoto sendPhoto) {
        log.info("Sending image message to user");
        telegramBot.execute(sendPhoto);
    }

    // метод выполняет отравку фото в стартовое сообщение пользователю
    public void sendImageMessageStart(SendPhoto sendPhoto) {
        log.info("Sending start image message to user");
        telegramBot.execute(sendPhoto);
    }

    // метод выполняет отравку видео пользователю
    public void sendVideo(SendVideo sendVideoRequest) {
        log.info("Sending video message to user");
        telegramBot.execute(sendVideoRequest);
    }

    // метод выполняет отравку пользователю документа
    public void sendDocument(Long chatId, SendDocument sendDoc) {
        log.info("Sending document message to user");
        telegramBot.execute(sendDoc);
    }
}
