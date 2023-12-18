package pro.sky.telegramBot.service.handlers.usersActionHandlers.impl;

import com.pengrad.telegrambot.model.Document;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.service.handlers.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.service.handlers.usersActionHandlers.DocumentHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.senders.DocumentSender;
import pro.sky.telegramBot.service.senders.HTMLMessageSender;
import pro.sky.telegramBot.service.senders.PhotoMessageSender;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.UserState.BLOCKED;
import static pro.sky.telegramBot.enums.UserState.PROBATION;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при отправке им какого-либо документа
 */
@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class DocumentActionHandler implements DocumentHandler {
    private final DocumentSender documentSender;
    private final HTMLMessageSender HTMLMessageSender;
    private final PhotoMessageSender photoMessageSender;
    private final UserService userService;
    private final BlockedUserHandler blockedUserHandler;

    @FunctionalInterface
    interface DocumentProcessor {
        void processDocument(Document document, Long chatId);
    }

    private final Map<String, DocumentProcessor> documentMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #documentMap} документами,<br>
     * при получении которых должен высылаться конкретный ответ: подтверждение получения, <br>
     * указание на ошибки при заполнении или другие сообщения
     */
    @PostConstruct
    public void init() {
        // Получаем отчет в формате .xlsx
        documentMap.put("report.xlsx", (document, chatId) -> {
            log.info("Processing report.xlsx document");
            // Проверяем, есть ли пользователь и может ли он присылать отчеты
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION) && user.getAdoptionRecord() != null) {
                try {
                    photoMessageSender.sendReportDocumentFromUserResponsePhotoMessage(document, chatId);
                } catch (IOException e) {
                    throw new UserNotFoundException("Пользователь не найден");
                }
            } else {
                //Сообщаем, что отсутствует запись об усыновлении
                HTMLMessageSender.sendNoAdoptionRecordHTMLMessage(chatId);
            }
        });

         // ключ принимает от пользователя заполненную таблицу с контактными данными
        documentMap.put("info_table.xlsx", (document, chatId) -> {
            log.info("Processing info_table.xlsx document");
            try {
                photoMessageSender.sendInfoTableDocumentFromUserResponsePhotoMessage(document, chatId);
            } catch (IOException e) {
                throw new UserNotFoundException("Пользователь не найден");
            }
        });

        // ключ принимает от пользователя PDF документ со кринами персональных документов
        documentMap.put("doc.pdf", (document, chatId) -> {
            log.info("Processing doc.pdf document");
            try {
                photoMessageSender.sendScreenPersonalDocumentsResponseMessage(document, chatId);
            } catch (IOException e) {
                throw new UserNotFoundException("Пользователь не найден");
            }
        });
    }

    @Override
    public void handle(Document document, Long chatId, UserState userState) {
        if (userState.equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
            return;
        }
        String fileName = document.fileName();
        DocumentProcessor documentProcessor = documentMap.get(fileName);
        if (documentProcessor != null) {
            documentProcessor.processDocument(document, chatId);
        } else {
            //Если не нашли обработчик для присланного документа
            log.warn("No handler found for the document: {}", fileName);
        }
    }
}
