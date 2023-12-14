package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;

public interface AdoptionRecordService {

    AdoptionRecord createNewAdoptionRecord(Long userId, Long petId);

    void addNewReportToAdoptionRecord(Report newReport, Long chatId);

    void checkNewAdopter();

    void informAdopterAboutStartReporting();

    void informAdopterAboutEndReporting();

    void save(AdoptionRecord adoptionRecord);

    void informAdopterAboutNeedToSendReport();

    void informAdopterAboutNeedToSendPhotoForReport();

    void decreaseTrialPeriodDays();

    AdoptionRecord extendAdoptionRecord(Long adoptionRecordId);

    AdoptionRecord terminateAdoptionRecord(Long adoptionRecordId);
}
