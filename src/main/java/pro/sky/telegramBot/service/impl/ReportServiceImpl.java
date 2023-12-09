package pro.sky.telegramBot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.QuestionsForReport;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.ReportDataConverter;
import pro.sky.telegramBot.utils.ReportSumCalculator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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
    private final ReportSumCalculator reportSumCalculator;
    private final TelegramBot bot;
    private final MediaLoader mediaLoader;

    @Override
    public boolean saveReport(Report newReport) {
        log.info("Was invoked method saveReport");
        reportRepository.save(newReport);
        return true;
    }

    @Override
    public boolean createReportFromExcel(Long chatId, List<String> values) {

        Report newReport = new Report();
        newReport.setData(new byte[0]);

        String dateString = values.get(5);
        newReport.setReportDateTime(reportDataConverter.convertToData(dateString));

        String valueA6 = values.get(4);
        int a6Int = reportDataConverter.convertToInteger(valueA6);
        newReport.setBehaviorChange(a6Int);

        String valueA8 = values.get(2);
        int a8Int = reportDataConverter.convertToInteger(valueA8);
        newReport.setDietAllergies(a8Int);

        String valueA10 = values.get(1);
        int a10Int = reportDataConverter.convertToInteger(valueA10);
        newReport.setDietPreferences(a10Int);

        String valueA12 = values.get(0);
        int a12Int = reportDataConverter.convertToInteger(valueA12);
        newReport.setDietAppetite(a12Int);

        String valueA14 = values.get(3);
        int a14Int = reportDataConverter.convertToInteger(valueA14);
        newReport.setHealthStatus(a14Int);

        int reportResult = reportSumCalculator.calculateReportSum(new int[]{a6Int, a8Int, a10Int, a12Int, a14Int});
        newReport.setRatingTotal(reportResult);

        adoptionRecordService.addNewReportToAdoptionRecord(newReport, chatId);

        return saveReport(newReport);
    }

    List<QuestionsForReport> questions = new ArrayList<>(Arrays.asList(QuestionsForReport.values()));
//Метод инициирует вопросы пользователю и сохраняет ответы в отчете
    @Override
    public void fillOutReport(Long chatId, String callbackData) {
        String[] parts = callbackData.split("_");
        int buttonIdentifier = Integer.parseInt(parts[0]);
        int questionIdentifier = Integer.parseInt(parts[1]);
        Long reportId = Long.parseLong(parts[2]);
        Report report = reportRepository.findById(reportId).orElseThrow();

        if (buttonIdentifier == 11) {
            log.info("Was invoked method of sending question number {} by ReportService", questionIdentifier);
            messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(0).getQuestion(), questionIdentifier, reportId);
        } else if (buttonIdentifier < 11) {
            switch (questionIdentifier) {
                case 0:
                    report.setDietAppetite(buttonIdentifier);
                    break;
                case 1:
                    report.setDietPreferences(buttonIdentifier);
                    break;
                case 2:
                    report.setDietAllergies(buttonIdentifier);
                    break;
                case 3:
                    report.setHealthStatus(buttonIdentifier);
                    break;
                case 4:
                    report.setBehaviorChange(buttonIdentifier);
                    reportRepository.save(report);
                    User user = userService.findUserByChatId(chatId);
                    user.setState(PROBATION);
                    userService.update(user);
                    messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(1).getQuestion(), 5, reportId);
                    adoptionRecordService.addNewReportToAdoptionRecord(report, chatId);
                    break;
            }
            reportRepository.save(report);

            int nextQuestionIdentifier = questionIdentifier + 1;
            if (nextQuestionIdentifier < questions.size()) {
                messageSender.sendQuestionForReportPhotoMessage(chatId, questions.get(nextQuestionIdentifier).getQuestion(), nextQuestionIdentifier, reportId);
            }
        }
    }
//Метод создает новый отчет, сохраняет его в базе и инициирует его заполнение
    @Override
    public void createReportOnline(Long chatId) {
        Report newReport = new Report();
        LocalDate date = LocalDate.now();
        newReport.setReportDateTime(date);
        newReport.setData(new byte[0]);
        reportRepository.save(newReport);
        Long reportId = newReport.getId();
        fillOutReport(chatId, "11_0_" + reportId);
    }
    @Override
    public void handlePetPhotoMessage(Long chatId, PhotoSize[] photo, Long reportId) {
        log.info("Was invoked handlePetPhotoMessage method for {}", chatId);
        Report report = reportRepository.findById(reportId).orElseThrow();
        String fileId = photo[0].fileId();
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(getFileRequest);

        if (getFileResponse.isOk()) {
            try {
                byte[] fileInputStream = bot.getFileContent(getFileResponse.file());
                byte[] resizedImage = mediaLoader.resizeReportPhoto(fileInputStream, 100);
                report.setData(resizedImage);
                log.info("Was invoked save method in report repository for {}", chatId);
                reportRepository.save(report);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException("Error while resizing image: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean attachPhotoToReport(Long chatId, PhotoSize[] photo) {
        log.info("Was invoked attachPhotoToReport method for {}", chatId);
        LocalDate date = LocalDate.now();
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getAdoptionRecord() != null){
            log.info("User and Adoption Report were found {}", chatId);
            Report report = reportRepository.findByAdoptionRecordIdAndReportDateTime(user.getAdoptionRecord().getId(), date).orElseThrow();
            handlePetPhotoMessage(chatId, photo, report.getId());
            log.info("should be true");
            return true;
        } else {
            log.info("User and Adoption Report not found {}", chatId);
            return false;
        }
    }
}
