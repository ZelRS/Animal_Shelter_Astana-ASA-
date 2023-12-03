package pro.sky.telegramBot.loader;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.reader.ExcelFileReader;

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
}
