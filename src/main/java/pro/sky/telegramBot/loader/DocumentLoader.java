package pro.sky.telegramBot.loader;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.reader.ExcelFileReader;
import pro.sky.telegramBot.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Класс для обработки и чтения документов
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class DocumentLoader {
    private final TelegramBot bot;
    private final ExcelFileReader excelFileReader;
    private final UserService userService;

    public List<String> readAdopterReport(Document document) {
        // Получаем параметры документа
        String fileId = document.fileId();

        // Преобразуем файл в InputStream
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(getFileRequest);

        byte[] fileInputStream = null;
        if (getFileResponse.isOk()) {
            try {
                fileInputStream = bot.getFileContent(getFileResponse.file());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return excelFileReader.getValues(fileInputStream);
    }

    public List<String> readInfoTable(Document document) {
        // Получаем параметры документа
        String fileId = document.fileId();

        // Преобразуем файл в InputStream
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(getFileRequest);

        byte[] fileInputStream = null;
        if (getFileResponse.isOk()) {
            try {
                fileInputStream = bot.getFileContent(getFileResponse.file());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return excelFileReader.getInfoTableValues(fileInputStream);
    }

    // метод не реализован до конца. ожидается создание таблицы волонтеров
    public void readAndSendScreenPersonalDocumentsToVolunteers(Document document, Long chatId) throws IOException {
        String fileId = document.fileId();
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(getFileRequest);
        File receivedFile = getFileResponse.file();

        byte[] fileContent = bot.getFileContent(receivedFile);

        List<User> users = userService.findAllByState(UserState.VOLUNTEER);
        if (!users.isEmpty()) {
            for (User user : users) {
                SendDocument sendDocument = new SendDocument(user.getChatId(), fileContent);
                sendDocument.fileName(chatId + "UserPersonalDocsScreens.pdf");
                sendDocument.contentType("pdf");
                bot.execute(sendDocument);
            }
        } else {
            SendMessage sendMessage = new SendMessage(chatId, "Извините, волонтеров нет...\nПопробуйте снова позже," +
                    " либо возьмите эти документы с собой в приют!");
            bot.execute(sendMessage);
        }
    }
}
