package pro.sky.telegramBot.utils.mediaUtils;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.loader.DocumentLoader;
import pro.sky.telegramBot.service.ReportService;

import java.io.IOException;
import java.util.List;

import static pro.sky.telegramBot.enums.MessageImage.*;

/**
 * Класс сбора параметров для сообщения пользователю при получении документа
 */

@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class SpecificDocumentMessageCreator {

    private final DocumentLoader documentLoader;
    private final ReportService reportService;
    private final BotConfig botConfig;
    private final MediaMessageCreator mediaMessageCreator;
    public SendPhoto createReportResponseMessage(Long chatId, Document document) throws IOException {

        //Получаем значения из отчета
        List<String> values = documentLoader.readAdopterReport(document);

        MediaMessageParams params = new MediaMessageParams();
        if (reportService.createReportFromExcel(chatId, values)) {
            params.setChatId(chatId);
            params.setFilePath(REPORT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(botConfig.getMSG_REPORT_ACCEPTED());
        } else {
            params.setChatId(chatId);
            params.setFilePath(REPORT_NOT_ACCEPTED_MSG_IMG.getPath());
            params.setCaption(botConfig.getMSG_REPORT_NOT_ACCEPTED());
        }
        return mediaMessageCreator.createPhotoMessage(params);
    }
}
