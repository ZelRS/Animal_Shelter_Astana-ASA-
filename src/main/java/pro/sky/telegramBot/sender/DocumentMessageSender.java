package pro.sky.telegramBot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.reader.ExcelFileReader;
import pro.sky.telegramBot.utils.mediaUtils.SpecificDocumentMessageCreator;

import java.io.IOException;

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
    private final MessageSender messageSender;
    private final ExcelFileReader excelFileReader;
    private final ReportService reportService;
    private final TelegramBot bot;
    private final SpecificDocumentMessageCreator specificDocumentMessageCreator;
    private final MessageExecutor messageExecutor;

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
}
