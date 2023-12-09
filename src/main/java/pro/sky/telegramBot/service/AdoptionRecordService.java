package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;

import java.time.LocalDate;

public interface AdoptionRecordService {
    Report getCurrentReport(Long id, LocalDate date);

    void addNewReportToAdoptionRecord(Report newReport, Long chatId);

    void checkNewAdopter();

    void informAdopterAboutStartReporting();

    void informAdopterAboutEndReporting();

    void save(AdoptionRecord adoptionRecord);
}
