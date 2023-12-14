package pro.sky.telegramBot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.QuestionsForReport;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.statistic.ReportDataConverter;
import pro.sky.telegramBot.utils.statistic.ReportSumCalculator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import static pro.sky.telegramBot.enums.UserState.PROBATION;


/**
 * Сервис для обработки отчетов пользователей
 */
@Service
@Transactional
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
    public boolean saveReport(Report newReport, Long chatId) {
        log.info("Was invoked method saveReport");
        LocalDate date = newReport.getReportDateTime();
        Report report = reportRepository.findByReportDateTime(date);
        if (date != null && report != null) {
            newReport.setId(report.getId());
        }
        adoptionRecordService.addNewReportToAdoptionRecord(newReport, chatId);
        reportRepository.save(newReport);
        return true;
    }
    //Метод создания отчета на основе данных эксель-файла

    @Override
    public boolean createReportFromExcel(Long chatId, List<String> values) {
        User user = userService.findUserByChatId(chatId);
        if (user == null || user.getAdoptionRecord() == null) {
            return false;
        }

        LocalDate date = reportDataConverter.convertToData(values.get(5));
        LocalDate startDate = user.getAdoptionRecord().getAdoptionDate();
        LocalDate endDate = LocalDate.now();

        if (!reportDataConverter.isDateWithinRange(date, startDate, endDate)) {
            user.setState(PROBATION);
            userService.update(user);
            return false;
        }

        Report newReport = getOrCreateReport(user, date);

        // Автоматизация установки значений с использованием индексированных полей из списка values
        setReportValues(newReport, values);

        reportRepository.save(newReport);
        calculateReportRatingTotal(newReport.getId());
        return true;
    }
//Метод для получения отчета, если такой уже есть, или создания нового отчета
    private Report getOrCreateReport(User user, LocalDate date) {
        AdoptionRecord adoptionRecord = user.getAdoptionRecord();
        Report report = reportRepository.findByAdoptionRecordIdAndReportDateTime(adoptionRecord.getId(), date);
        if (report == null) {
            report = new Report();
            report.setReportDateTime(date);
            report.setAdoptionRecord(adoptionRecord);
        }
        return report;
    }
//В этом методу мы присваиваем значения. Если значения в отчете не указаны, то будет присвоен ноль.
    private void setReportValues(Report report, List<String> values) {
        int[] fieldsIndexes = {0, 1, 2, 3, 4};
        List<Consumer<Integer>> valueSetters = Arrays.asList(
                report::setHealthStatus,
                report::setDietAllergies,
                report::setDietPreferences,
                report::setBehaviorChange,
                report::setDietAppetite
        );

        for (int i = 0; i < fieldsIndexes.length; i++) {
            String valueStr = values.get(fieldsIndexes[i]);
            int valueInt = 0;
            if (valueStr != null && !valueStr.isEmpty()) {
                try {
                    valueInt = reportDataConverter.convertToInteger(valueStr);
                } catch (NumberFormatException e) {
                    // Оставляем valueInt равным 0, если конвертация не удалась
                }
            }
            // Настраиваем значение. Если valueStr был недопустим, valueInt останется 0.
            valueSetters.get(i).accept(valueInt);
        }
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
                    calculateReportRatingTotal(reportId);
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
        User user = userService.findUserByChatId(chatId);
        LocalDate date = LocalDate.now();
        Report newReport = null;
        if (user != null && user.getAdoptionRecord() != null) {
            AdoptionRecord adoptionRecord = user.getAdoptionRecord();
            newReport = reportRepository.findByAdoptionRecordIdAndReportDateTime(adoptionRecord.getId(), date);
            if (newReport == null) {
                newReport = new Report();
                newReport.setReportDateTime(date);
                newReport.setAdoptionRecord(adoptionRecord);
                reportRepository.save(newReport);
            }
            Long reportId = newReport.getId();
            log.info("Was set report id {}", reportId);
            fillOutReport(chatId, "11_0_" + reportId);
        }
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
                byte[] resizedImage = mediaLoader.resizeReportPhoto(fileInputStream, 300);
                report.setData(resizedImage);
                log.info("Was invoked save method in report repository for {}", chatId);
                reportRepository.save(report);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException("Error while resizing image: " + e.getMessage());
            }
        }
        User user = userService.findUserByChatId(chatId);
        user.setState(PROBATION);
        userService.update(user);
    }

    @Override
    public boolean attachPhotoToReport(Long chatId, PhotoSize[] photo) {
        log.info("Was invoked attachPhotoToReport method for {}", chatId);
        LocalDate date = LocalDate.now();
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getAdoptionRecord() != null) {
            log.info("User and Adoption Report were found {}", chatId);
            Report report = reportRepository.findByAdoptionRecordIdAndReportDateTime(user.getAdoptionRecord().getId(), date);
            if (report != null) {
                handlePetPhotoMessage(chatId, photo, report.getId());
                log.info("should be true");
                return true;
            } else {
                log.info("Today Report not found {}", chatId);
                user.setState(PROBATION);
                userService.update(user);
                return false;
            }
        }
        log.info("User or Adoption Report not found {}", chatId);
        return false;
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Report with id " + id + " was not found"));
    }


    private void calculateReportRatingTotal(Long reportId) {
        try {
            Report report = reportRepository.findById(reportId).orElseThrow();
            int a6Int = report.getDietAppetite();
            int a8Int = report.getDietPreferences();
            int a10Int = report.getDietAllergies();
            int a12Int = report.getHealthStatus();
            int a14Int = report.getBehaviorChange();
            int reportResult = reportSumCalculator.calculateReportSum(new int[]{a6Int, a8Int, a10Int, a12Int, a14Int});
            report.setRatingTotal(reportResult);
            reportRepository.save(report);
        } catch (NoSuchElementException e) {
            log.error("Ошибка при поиске отчета по идентификатору: {}", reportId);
        }
    }

}
