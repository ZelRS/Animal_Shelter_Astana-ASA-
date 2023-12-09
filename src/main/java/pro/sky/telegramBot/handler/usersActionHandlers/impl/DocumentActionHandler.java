package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import com.pengrad.telegrambot.model.Document;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.DocumentHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.specificSenders.DocumentMessageSender;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.UserService;

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

    private final DocumentMessageSender documentMessageSender;
    private final UserService userService;
    private final MessageSender messageSender;
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
                    documentMessageSender.sendReportResponseMessage(document, chatId);
                } catch (IOException e) {
                    throw new UserNotFoundException("Пользователь не найден");
                }
            } else {
                //Сообщаем, что отсутствует запись об усыновлении
                messageSender.sendNoAdoptionRecordMessage(chatId);
            }
        });

         // ключ принимает от пользователя заполненную таблицу с контактными данными
        documentMap.put("info_table.xlsx", (document, chatId) -> {
            log.info("Processing info_table.xlsx document");
            try {
                documentMessageSender.sendInfoTableResponseMessage(document, chatId);
            } catch (IOException e) {
                throw new UserNotFoundException("Пользователь не найден");
            }
        });

        // ключ принимает от пользователя PDF документ со кринами персональных документов
        documentMap.put("doc.pdf", (document, chatId) -> {
            log.info("Processing doc.pdf document");
            try {
                documentMessageSender.sendScreenPersonalDocumentsResponseMessage(document, chatId);
            } catch (IOException e) {
                throw new UserNotFoundException("Пользователь не найден");
            }
        });
    }

    @Override
    public void handle(Document document, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getState().equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
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
