package pro.sky.telegramBot.service.loaders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.ExcelFileReader;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

// Определяем сервис с необходимыми зависимостями для работы с документами.
/**
 * Класс для обработки и чтения документов
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DocumentLoader {
    // Бот API для телеграма.
    private final TelegramBot bot;
    // Сервис для чтения из Excel файлов.
    private final ExcelFileReader excelFileReader;
    // Сервис управления пользователями.
    private final UserService userService;

    // Метод для чтения отчета об усыновлении (adoption report).
    public List<String> readAdopterReport(Document document) {
        // Получаем содержимое файла.
        byte[] fileInputStream = getFileContent(document);
        // Читаем значения из файла.
        return excelFileReader.getValues(fileInputStream);
    }

    // Метод для чтения информационной таблицы.
    public List<String> readInfoTable(Document document) {
        // Получаем содержимое файла.
        byte[] fileInputStream = getFileContent(document);
        // Читаем значения из файла.
        return excelFileReader.getInfoTableValues(fileInputStream);
    }

    // Метод для чтения и отправки персональных документов волонтерам.
    public void readAndSendScreenPersonalDocumentsToVolunteers(Document document, Long chatId) {
        // Получаем содержимое файла.
        byte[] fileContent = getFileContent(document);

        // Находим всех пользователей с состоянием VOLUNTEER.
        List<User> users = userService.findAllByState(UserState.VOLUNTEER);
        // Если список не пуст, рассылаем документы, иначе сообщаем об отсутствии волонтеров.
        if (!users.isEmpty()) {
            users.parallelStream().forEach(user -> sendDocument(chatId, fileContent, user));
        } else {
            sendNoVolunteersMessage(chatId);
        }
    }

    // Вспомогательный метод для получения содержимого файла.
    private byte[] getFileContent(Document document) {
        // Создаем запрос на получение файла.
        GetFile getFileRequest = new GetFile(document.fileId());
        // Выполняем запрос.
        GetFileResponse getFileResponse = bot.execute(getFileRequest);

        // Если запрос успешен, пытаемся получить содержимое файла.
        if (getFileResponse.isOk()) {
            try {
                return bot.getFileContent(getFileResponse.file());
            } catch (IOException e) {
                // В случае ошибки, логируем и выбрасываем исключение.
                log.error("Error occurred while reading the file", e);
                throw new RuntimeException("Error occurred while processing the file.", e);
            }
        } else {
            // Если не удаётся получить файл, логируем ошибку и выбрасываем исключение.
            log.error("Unable to get the file from Telegram");
            throw new RuntimeException("Error occurred while downloading the file.");
        }
    }

    // Метод для отправки документа пользователю.
    private void sendDocument(Long chatId, byte[] fileContent, User user) {
        // Создание объекта для отправки документа.
        SendDocument sendDocument = new SendDocument(user.getChatId(), fileContent);
        // Установка имени файла и типа контента.
        sendDocument.fileName(chatId + "UserPersonalDocsScreens.pdf");
        sendDocument.contentType("application/pdf");
        // Выполнение отправки документа.
        bot.execute(sendDocument);
    }

    // Метод для отправки сообщения о том, что волонтеры отсутствуют.
    private void sendNoVolunteersMessage(Long chatId) {
        // Создание объекта сообщения.
        SendMessage sendMessage = new SendMessage(chatId, "Извините, волонтеров нет...\nПопробуйте снова позже, " +
                                                          "либо возьмите эти документы с собой в приют!");
        // Отправка сообщения.
        bot.execute(sendMessage);
    }
}