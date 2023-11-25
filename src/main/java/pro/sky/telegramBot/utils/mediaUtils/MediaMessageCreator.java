package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.MediaMessageParams;

import java.io.IOException;

// класс содержит функционал, закрепляюищий определенный медиа-контент за сообщением
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class MediaMessageCreator {
    private final MediaLoader mediaLoader;

    // метод для закрепления загруженноого фото за сообщением
    public SendPhoto createPhotoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.imageCreator(params.getChatId(), params.getFilePath(), params.getCaption());
    }

    // метод для закрепления загруженноого видео за сообщением
    public SendVideo createVideoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.videoCreator(params.getChatId(), params.getFilePath(), params.getFileName());
    }

    // метод для закрепления загруженноого документа за сообщением
    public SendDocument createDocumentMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.documentCreator(params.getChatId(), params.getFilePath(), params.getFileName());
    }
}
