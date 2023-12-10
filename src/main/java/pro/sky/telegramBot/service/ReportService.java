package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.Adoption.Report;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import pro.sky.telegramBot.model.Adoption.Report;

import java.util.List;
import java.util.Optional;

public interface ReportService {
    boolean saveReport(Report newReport, Long chatId);

    boolean createReportFromExcel(Long chatId, List<String> values);

    void fillOutReport(Long chatId, String callbackData);

    void createReportOnline(Long chatId);

    void handlePetPhotoMessage(Long chatId, PhotoSize[] photo, Long reportId);

    boolean attachPhotoToReport(Long chatId, PhotoSize[] photo);

    Report getReportById(Long id);
}
