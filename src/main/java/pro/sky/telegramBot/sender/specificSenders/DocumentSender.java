package pro.sky.telegramBot.sender.specificSenders;

import com.pengrad.telegrambot.request.SendDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

/**
 * методы класса группируют компоненты и формируют документ, который<br>
 * затем с помощью {@link #executor}, отправляется пользователю<br>
 * в ответ на его команду
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class DocumentSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor executor;

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/report", если пользователь взял питомца
     */
    // пользователь получает документ в формате xlsx для заполнения отчета
    // у пользователя должен быть статус PROBATION
    public void sendReportToUserDocument(Long chatId) {
        log.info("Sending report document message to {}", chatId);
        SendDocument sendDocument = null;
        try {
            sendDocument = specificMediaMessageCreator.createReportSendDocumentMessage(chatId);
        } catch (Exception e) {
            log.error("Failed to send report document message to {}", chatId, e);
        }
        executor.executeDocument(sendDocument);
    }

    /**
     * метод формирует и отправляет файл пользователю,<br>
     * когда он нажимает на ссылку рядом с выбранным документом
     */
    public void sendRecDocDocument(Integer refNum, Long chatId) {
        log.info("Sending recommendation document to {}", chatId);
        try {
            SendDocument sendDoc = specificMediaMessageCreator.createRecDocDocumentMessage(refNum, chatId);
            executor.executeDocument(sendDoc);
        } catch (Exception e) {
            log.error("Failed to send recommendation document to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/info_table"
     */
    // пользователь получает документ в формате xlsx для введения контактных данных
    public void sendInfoTableToUserDocumentRequestMessage(Long chatId) {
        log.info("Sending info_table document message to {}", chatId);
        try {
            SendDocument document = specificMediaMessageCreator.createInfoTableDocumentMessage(chatId);
            document.fileName("info_table.xlsx");
            executor.executeDocument(document);
        } catch (Exception e) {
            log.error("Failed to send info_table document message to {}", chatId, e);
        }
    }
}
