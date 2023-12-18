package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;

public interface AdoptionRecordService {

    /**
     * Метод позволяет создать новую запись об усыновлении.
     *
     * @param userId идентификатор пользователя
     * @param petId  идентификатор питомца
     * @return созданный экземпляр AdoptionRecord
     */
    AdoptionRecord createNewAdoptionRecord(Long userId, Long petId);
    /**
     * Метод добавляет новый отчет к записи об усыновлении.
     *
     * @param newReport новый отчет
     * @param chatId    идентификатор чата
     */
    void addNewReportToAdoptionRecord(Report newReport, Long chatId);
    /**
     * Метод проверяет наличие новых заявителей на усыновление и производит необходимые действия по их обработке.
     */
    void checkNewAdopter();
    /**
     * Метод информирует заявителя об усыновлении о начале онлайн составления отчета.
     */
    void informAdopterAboutStartReporting();
    /**
     * Метод информирует заявителя об усыновлении о завершении составления онлайн отчета.
     */
    void informAdopterAboutEndReporting();
    /**
     * Метод сохраняет запись об усыновлении.
     *
     * @param adoptionRecord запись об усыновлении
     */
    void save(AdoptionRecord adoptionRecord);
    /**
     * Метод информирует заявителя об усыновлении о необходимости отправить отчет.
     */
    void informAdopterAboutNeedToSendReport();
    /**
     * Метод информирует заявителя об усыновлении о необходимости отправить фотографию для отчета.
     */
    void informAdopterAboutNeedToSendPhotoForReport();
    /**
     * Метод уменьшает количество оставшихся дней испытательного срока у записи об усыновлении.
     */
    void decreaseTrialPeriodDays();
    /**
     * Метод продлевает запись об усыновлении.
     *
     * @param adoptionRecordId идентификатор записи об усыновлении
     * @return обновленный экземпляр AdoptionRecord
     */
    AdoptionRecord extendAdoptionRecord(Long adoptionRecordId);
    /**
     * Метод прекращает запись об усыновлении.
     *
     * @param adoptionRecordId идентификатор записи об усыновлении
     * @return обновленный экземпляр AdoptionRecord
     */
    AdoptionRecord terminateAdoptionRecord(Long adoptionRecordId);
}
