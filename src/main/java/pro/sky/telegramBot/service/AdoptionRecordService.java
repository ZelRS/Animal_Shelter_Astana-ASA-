package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.enums.TrialPeriodState;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
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

    AdoptionRecord createNewAdoptionRecord(Long userId, Long petId);

    void addNewReportToAdoptionRecord(Report newReport, Long chatId);

    void checkNewAdopter();

    void informAdopterAboutStartReporting();

    void informAdopterAboutEndReporting();

    void save(AdoptionRecord adoptionRecord);

    void informAdopterAboutNeedToSendReport();

    void informAdopterAboutNeedToSendPhotoForReport();

    void decreaseTrialPeriodDaysAndCheckEvents();

    AdoptionRecord extendAdoptionRecord(Long adoptionRecordId);

    AdoptionRecord terminateAdoptionRecord(Long adoptionRecordId);
}
