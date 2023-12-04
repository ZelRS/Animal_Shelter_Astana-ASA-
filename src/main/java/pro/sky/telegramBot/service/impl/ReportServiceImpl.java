package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.QuestionsForReport;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.ReportDataConverter;

import java.time.LocalDate;
import java.util.*;

import static pro.sky.telegramBot.entity.Button.CallbackData.BUT_FILL_OUT_REPORT_ON;
import static pro.sky.telegramBot.enums.UserState.PROBATION;


/**
 * Сервис для обработки отчетов пользователей
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportDataConverter reportDataConverter;
    private final UserService userService;
    private final AdoptionRecordService adoptionRecordService;
    private final MessageSender messageSender;

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
//Метод инициирует вопросы пользователю и сохраняет ответы в отчете
    @Override
    public void fillOutReport(Long chatId, String callbackData) {
        String[] parts = callbackData.split("_");
        Long reportId = Long.parseLong(parts[2]);
        int questionIdentifier = Integer.parseInt(parts[1]);
        int buttonIdentifier = Integer.parseInt(parts[0]);
        Report report = reportRepository.findById(reportId).orElseThrow();
        List<QuestionsForReport> questions = new ArrayList<>(Arrays.asList(QuestionsForReport.values()));
        if (buttonIdentifier == 11) {
            log.info("Was invoked method of sending question number {} by ReportService", questionIdentifier);
            messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(0).getQuestion(), questionIdentifier, reportId);
        } else if (buttonIdentifier < 11) {
            switch (questionIdentifier) {
                case 0:
                    report.setDietAppetite(buttonIdentifier);
                    reportRepository.save(report);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(1).getQuestion(), 1, reportId);
                    break;
                case 1:
                    report.setDietPreferences(buttonIdentifier);
                    reportRepository.save(report);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(2).getQuestion(), 2, reportId);
                    break;
                case 2:
                    report.setDietAllergies(buttonIdentifier);
                    reportRepository.save(report);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(3).getQuestion(), 3, reportId);
                    break;
                case 3:
                    report.setHealthStatus(buttonIdentifier);
                    reportRepository.save(report);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(4).getQuestion(), 4, reportId);
                    break;
                case 4:
                    report.setBehaviorChange(buttonIdentifier);
                    reportRepository.save(report);
                    User user = userService.findUserByChatId(chatId);
                    user.setState(PROBATION);
                    userService.update(user);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(1).getQuestion(), 5, reportId);
                    break;
            }
        }
    }
//Метод создает новый отчет, сохраняет его в базе и инициирует его заполнение
    @Override
    public void createReportOnline(Long chatId) {
        Report newReport = new Report();
        LocalDate date = LocalDate.now();
        newReport.setReportDateTime(date);
        reportRepository.save(newReport);
        Long reportId = newReport.getId();
        fillOutReport(chatId, "11_0_" + reportId);
    }
}
