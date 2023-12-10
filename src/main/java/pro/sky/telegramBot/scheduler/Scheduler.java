package pro.sky.telegramBot.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.service.AdoptionRecordService;

/**
 * Методы класса устанавливают время, когда должны запускаться
 * определенные методы
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class Scheduler {
    private final AdoptionRecordService adoptionRecordService;

    //Запускаем проверку новых усыновителей
    @Scheduled(cron = "0 00 17 * * *")
    public void checkNewAdopter() {
        adoptionRecordService.checkNewAdopter();
    }

    //Запускаем напоминание о начале заполнения отчета онлайн
    @Scheduled(cron = "0 50 17 * * *")
    public void informAdopterAboutStartReporting() {
        adoptionRecordService.informAdopterAboutStartReporting();
    }

    //Запускаем напоминание о завершении заполнения отчета онлайн
    @Scheduled(cron = "0 50 20 * * *")
    public void informAdopterAboutEndReporting() {
        adoptionRecordService.informAdopterAboutEndReporting();
    }

    //Запускаем напоминание о необходимости прислать отчет
    @Scheduled(cron = "0 00 21 * * *")
    public void informAdopterAboutNeedToSendReport() {
        adoptionRecordService.informAdopterAboutNeedToSendReport();
    }
    //Запускаем напоминание о необходимости прислать фото для отчета
    @Scheduled(cron = "0 05 21 * * *")
    public void informAdopterAboutNeedToSendPhotoForReport() {
        adoptionRecordService.informAdopterAboutNeedToSendPhotoForReport();
    }
    @Scheduled(cron = "0 00 00 * * *")
    public void decreaseTrialPeriodDaysAndCheckEvents() {
        adoptionRecordService.decreaseTrialPeriodDaysAndCheckEvents();
    }
}
