package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.enums.TrialPeriodState;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.users.User;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface AdoptionRecordService {
    Report getCurrentReport(Long id, LocalDate date);

    AdoptionRecord createNewAdoptionRecord(Long userId, Integer trialPeriodDays, Long petId);

    void addNewReportToAdoptionRecord(Report newReport, Long chatId);

    void checkNewAdopter();

    void informAdopterAboutStartReporting();

    void informAdopterAboutEndReporting();

    void save(AdoptionRecord adoptionRecord);

    void informAdopterAboutNeedToSendReport();

    void informAdopterAboutNeedToSendPhotoForReport();
}
