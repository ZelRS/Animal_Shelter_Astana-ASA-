package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.Adoption.Report;

import java.util.List;

public interface ReportService {
    boolean saveReport(Report newReport);

    boolean createReportFromExcel(Long chatId, List<String> values);
}
