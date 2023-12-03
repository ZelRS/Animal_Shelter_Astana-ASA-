package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.utils.ReportDataConverter;

import java.util.List;


/**
 * Сервис для обработки отчетов пользователей
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportDataConverter reportDataConverter;
    @Override
    public boolean saveReport(Report newReport) {
        log.info("Was invoked method saveReport");
        reportRepository.save(newReport);
        return true;
    }

    @Override
    public boolean createReportFromExcel(Long chatId, List<String> values) {

        Report newReport = new Report();

        String dateString = values.get(5);
        newReport.setReportDateTime(reportDataConverter.convertToData(dateString));

        String valueA6 = values.get(4);
        newReport.setBehaviorChange(reportDataConverter.convertToInteger(valueA6));

        String valueA8 = values.get(2);
        newReport.setDietAllergies(reportDataConverter.convertToInteger(valueA8));

        String valueA10 = values.get(1);
        newReport.setDietPreferences(reportDataConverter.convertToInteger(valueA10));

        String valueA12 = values.get(0);
        newReport.setDietAppetite(reportDataConverter.convertToInteger(valueA12));

        String valueA14 = values.get(3);
        newReport.setHealthStatus(reportDataConverter.convertToInteger(valueA14));

        return saveReport(newReport);
    }
}
