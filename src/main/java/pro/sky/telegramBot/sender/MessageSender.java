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

@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class MessageSender {
    private final TelegramBot telegramBot;

    private final Map<Long, String> remainingMessages = new ConcurrentHashMap<>();

    public void sendHTMLMessage(long chatId, String messageText) {
        log.info("Sending HTML text message to user");
            SendMessage message = new SendMessage(String.valueOf(chatId), messageText).parseMode(HTML);
            telegramBot.execute(message);
    }

    public void sendImageMessage(SendPhoto sendPhoto) {
        log.info("Sending image message to user");
            telegramBot.execute(sendPhoto);
    }
    public void sendImageMessageStart(SendPhoto sendPhoto) {
        log.info("Sending start image message to user");
        telegramBot.execute(sendPhoto);
    }

    public void executeMessage(SendMessage message) {
        log.info("Message executing to user");
        telegramBot.execute(message);
    }

    public void sendDocument(Long chatId, SendDocument sendDoc) {
        log.info("Sending document message to user");
        telegramBot.execute(sendDoc);
    }

    public void sendVideo(SendVideo sendVideoRequest) {
        log.info("Sending video message to user");
        telegramBot.execute(sendVideoRequest);
    }
}
