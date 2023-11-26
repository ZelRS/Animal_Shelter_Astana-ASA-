package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.model.request.InputFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

// класс с функционалом загрузки медиа-контента для последующего добавления его к сообщению
@Service
@Slf4j  // SLF4J logging
public class MediaLoader {
    // метод загрузки фотограифии
    public SendPhoto imageLoader(Long chatId, String path, String message) throws IOException {
        log.info("Creating send photo object");
        InputStream imageStream = getClass().getResourceAsStream(path);
        assert imageStream != null;
        byte[] imageBytes = imageStream.readAllBytes();
        imageStream.close();

        SendPhoto sendPhoto = new SendPhoto(chatId, imageBytes);
        return sendPhoto.caption(message).parseMode(HTML);
    }

    // метод загрузки видео
    public SendVideo videoLoader(Long chatId, String filePath, String fileName) throws IOException {
        log.info("Creating send mp4 video object");
        InputStream videoStream = getClass().getResourceAsStream(filePath);

        File tempFile = File.createTempFile("video", ".mp4");
        assert videoStream != null;
        Files.copy(videoStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        InputFile video = new InputFile(tempFile, fileName, "video/mp4");

        return new SendVideo(chatId, video.getFile());
    }

    // метод загрузки документа
    public SendDocument documentLoader(Long chatId, String filePath, String fileName) throws IOException {
        log.info("Creating send pdf document object");
        InputStream fileStream = getClass().getResourceAsStream(filePath);

        File tempFile = File.createTempFile("SoftGPT", ".pdf");
        assert fileStream != null;
        Files.copy(fileStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        InputFile SoftGPT = new InputFile(tempFile, fileName, "application/pdf");

        return new SendDocument(chatId, SoftGPT.getFile());
    }
}
