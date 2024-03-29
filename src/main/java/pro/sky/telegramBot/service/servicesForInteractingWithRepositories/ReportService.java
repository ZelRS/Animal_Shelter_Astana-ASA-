package pro.sky.telegramBot.service.servicesForInteractingWithRepositories;

import com.pengrad.telegrambot.model.PhotoSize;
import pro.sky.telegramBot.model.adoption.Report;

import java.util.List;

public interface ReportService {
    boolean saveReport(Report newReport, Long chatId);

    boolean createReportFromExcel(Long chatId, List<String> values);

    void fillOutReport(Long chatId, String callbackData);

    void createReportOnline(Long chatId);

    void handlePetPhotoMessage(Long chatId, PhotoSize[] photo, Long reportId);

    boolean attachPhotoToReport(Long chatId, PhotoSize[] photo);

    Report getReportById(Long id);

}
