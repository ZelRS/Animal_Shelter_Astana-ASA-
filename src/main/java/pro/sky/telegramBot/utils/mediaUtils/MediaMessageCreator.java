package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.MediaMessageParams;

import java.io.IOException;

/**
 * класс содержит логику, закрепляющую загружаемый медиа-контент за сообщением
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class MediaMessageCreator {
    private final MediaLoader mediaLoader;

    /**
     * закрепление загруженного фото за сообщением
     */
    public SendPhoto createPhotoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.imageLoader(params.getChatId(), params.getFilePath(), params.getCaption());
    }

    /**
     * закрепление загруженного видео за сообщением
     */
    public SendVideo createVideoMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.videoLoader(params.getChatId(), params.getFilePath(), params.getFileName());
    }

    /**
     * закрепление загруженного документа за сообщением
     */
    public SendDocument createDocumentMessage(MediaMessageParams params) throws IOException {
        return mediaLoader.documentLoader(params.getChatId(), params.getFilePath(), params.getFileName());
    }
}
