package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegramBot.exception.notFound.PetNotFoundException;
import pro.sky.telegramBot.exception.notFound.UserNotFoundException;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.AdoptionRecordRepository;
import pro.sky.telegramBot.sender.specificSenders.NotificationSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.PetService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.statistic.ReportAnalyser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static pro.sky.telegramBot.enums.PetType.NOPET;
import static pro.sky.telegramBot.model.adoption.AdoptionRecord.TrialPeriodState.*;
import static pro.sky.telegramBot.enums.UserState.*;
import static pro.sky.telegramBot.enums.UserState.PROBATION;
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionRecordServiceImpl implements AdoptionRecordService {
    private final AdoptionRecordRepository adoptionRecordRepository;
    private final UserService userService;
    private final PetService petService;
    private final NotificationSender notificationSender;
    private final ReportAnalyser reportAnalyser;

    @Override
    public AdoptionRecord createNewAdoptionRecord(Long userId, Long petId) {
        User user = userService.getById(userId);
        LocalDate date = LocalDate.now();
        int trialPeriodDays = 30;
        if (user == null) {
            log.error("No user was found by id {}", userId);
            throw new UserNotFoundException("No user was found");
        }
        if (user.getAdoptionRecord() != null && !user.getAdoptionRecord().getState().equals(SUCCESSFUL)) {
            log.error("Is is not possible to create a new adoption record for user {}", userId);
            throw new UserNotFoundException("No user was found");
        }
        AdoptionRecord newAdoptionRecord = new AdoptionRecord();
        Pet pet = petService.getById(petId);
        if (pet == null) {
            log.error("No pet was found by id {}", petId);
            throw new PetNotFoundException("No user was found");
        }
        newAdoptionRecord.setUser(user);
        newAdoptionRecord.setPet(pet);
        newAdoptionRecord.setState(AdoptionRecord.TrialPeriodState.PROBATION);
        newAdoptionRecord.setAdoptionDate(date);
        newAdoptionRecord.setTrialPeriodDays(trialPeriodDays);
        newAdoptionRecord.setTrialPeriodEnd(date.plusDays(trialPeriodDays));

        AdoptionRecord savedAdoptionRecord = adoptionRecordRepository.save(newAdoptionRecord);

        user.setState(PROBATION);
        user.setAdoptionRecord(savedAdoptionRecord);
        user.setPet(pet);
        userService.update(user);
        pet.setOwner(user);
        pet.setAdoptionRecord(savedAdoptionRecord);
        petService.update(pet);

        return savedAdoptionRecord;

    }
    @Override
    public AdoptionRecord extendAdoptionRecord(Long adoptionRecordId) {
        try {
            AdoptionRecord adoptionRecord = adoptionRecordRepository.findById(adoptionRecordId)
                    .orElseThrow(() -> new NoSuchElementException("Adoption record not found"));

            if(!adoptionRecord.getState().equals(PROBATION_EXTEND)){
                log.error("No adoption record can be extended for recordID: {}", adoptionRecordId);
                throw new IllegalStateException("No adoption record can be extended");
            }

        User user = adoptionRecord.getUser();
        Pet pet = adoptionRecord.getPet();
        LocalDate date = LocalDate.now();
        int trialPeriodDays = 14;

        AdoptionRecord extendetAdoptionRecord = new AdoptionRecord();

        extendetAdoptionRecord.setUser(user);
        extendetAdoptionRecord.setPet(pet);
        extendetAdoptionRecord.setState(PROBATION_EXTEND);
        extendetAdoptionRecord.setAdoptionDate(date);
        extendetAdoptionRecord.setTrialPeriodDays(trialPeriodDays);
        extendetAdoptionRecord.setTrialPeriodEnd(date.plusDays(trialPeriodDays));

        AdoptionRecord savedAdoptionRecord = adoptionRecordRepository.save(extendetAdoptionRecord);

        user.setAdoptionRecord(savedAdoptionRecord);
        userService.update(user);
        pet.setAdoptionRecord(savedAdoptionRecord);
        petService.update(pet);
        adoptionRecord.setState(CLOSED);
        adoptionRecordRepository.save(adoptionRecord);

            return savedAdoptionRecord;
        } catch (NoSuchElementException | IllegalStateException e) {
            log.error("Error extending adoption record: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public AdoptionRecord terminateAdoptionRecord(Long adoptionRecordId) {
        try {
            AdoptionRecord adoptionRecord = adoptionRecordRepository.findById(adoptionRecordId)
                    .orElseThrow(() -> new NoSuchElementException("Adoption record not found"));

        User user = adoptionRecord.getUser();
        Pet pet = adoptionRecord.getPet();
        adoptionRecord.setState(CLOSED);
        adoptionRecordRepository.save(adoptionRecord);
        user.setState(UNTRUSTED);
        user.setPet(null);
        userService.update(user);
        pet.setOwner(null);
        petService.update(pet);

        return adoptionRecord;
        } catch (NoSuchElementException | IllegalStateException e) {
            log.error("Error extending adoption record: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void save(AdoptionRecord adoptionRecord) {
        adoptionRecordRepository.save(adoptionRecord);
    }

    /**
     * Метод заполняет запись об усыновлении
     */
    private void setAdoptionRecordForUser(User user) {
        if (user != null && user.getAdoptionRecord() != null) {
            LocalDate date = LocalDate.now();
            Pet pet = user.getPet();
            AdoptionRecord adoptionRecord = user.getAdoptionRecord();
            adoptionRecord.setAdoptionDate(date);
            adoptionRecord.setState(AdoptionRecord.TrialPeriodState.PROBATION);
            adoptionRecord.setRatingTotal(0);
            if (pet != null) {
                adoptionRecord.setPet(pet);
            } else {
                pet = new Pet();
                pet.setType(NOPET);
                adoptionRecord.setPet(pet);
                List<User> volunteers = userService.findAllByState(VOLUNTEER);
                for (User volunteer : volunteers) {
                    notificationSender.sendMissingPetMessageToVolunteerPhotoMessage(user.getChatId(), volunteer.getChatId());
                }
            }
            adoptionRecordRepository.save(adoptionRecord);
        }
    }

    /**
     * Метод добавляет новый отчет к записи усыновления
     */
    @Override
    public void addNewReportToAdoptionRecord(Report newReport, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getAdoptionRecord() != null) {
            AdoptionRecord adoptionRecord = user.getAdoptionRecord();
            List<Report> reports = adoptionRecord.getReports();
            if (reports == null) {
                reports = new ArrayList<>();
                adoptionRecord.setReports(reports);
                adoptionRecordRepository.save(adoptionRecord);
            }
            newReport.setAdoptionRecord(adoptionRecord);
            adoptionRecordRepository.save(adoptionRecord);
        }
    }

    /**
     * Метод находит новых усыновителей, создает запись об усыновлении и отправляет сообщение
     * о возможности онлайн заполнения отчета
     */
    @Override
    public void checkNewAdopter() {
        List<User> newAdopters = userService.findAllByAdoptionRecordIsNullAndState(PROBATION);
        if (!newAdopters.isEmpty()) {
            for (User user : newAdopters) {
                user.setAdoptionRecord(new AdoptionRecord());
                setAdoptionRecordForUser(user);
                userService.update(user);
                notificationSender.sendNotificationToAdopterAboutDailyReportPhotoMessage(user.getChatId());
            }
        }
    }

    /**
     * Метод находит усыновителей и запускает отправку сообщения о начале процедуры онлайн заполнения отчета
     */
    @Override
    public void informAdopterAboutStartReporting() {
        List<User> adopters = userService.findAllByState(PROBATION);
        if (!adopters.isEmpty()) {
            for (User user : adopters) {
                notificationSender.sendNotificationToAdopterAboutStartReportPhotoMessage(user.getChatId());
            }
        }

    }

    /**
     * Метод находит усыновителей и запускает отправку сообщения о завершении процедуры онлайн заполнения отчета
     */
    @Override
    public void informAdopterAboutEndReporting() {
        List<User> adopters = userService.findAllByState(PROBATION);
        if (!adopters.isEmpty()) {
            for (User user : adopters) {
                notificationSender.sendNotificationToAdopterAboutEndReportPhotoMessage(user.getChatId());
            }
        }
    }

    /**
     * Метод находит усыновителей, которые не прислали отчет, и запускает отправку сообщения
     * о необходимости прислать отчет
     */
    @Override
    public void informAdopterAboutNeedToSendReport() {
//        List<User> adopters = adoptionRecordRepository.findUsersWithProbationAndNoReportToday();
//        if (!adopters.isEmpty()) {
//            for (User user : adopters) {
//                notificationSender.sendNotificationToAdopterAboutNeedToSendReportPhotoMessage(user.getChatId());
//            }
//        }
    }

    /**
     * Метод находит усыновителей, которые прислали отчет, но не прислали фото, и запускает отправку
     * сообщения о необходимости прислать фото
     */
    @Override
    public void informAdopterAboutNeedToSendPhotoForReport() {
//        List<User> adopters = adoptionRecordRepository.findUsersWithReportTodayAndNoPhoto();
//        if (!adopters.isEmpty()) {
//            for (User user : adopters) {
//                notificationSender.sendNotificationToAdopterAboutNeedToSendPhotoForReportPhotoMessage(user.getChatId());
//            }
//        }
    }
    /**
     * Метод следит за уменьшением остатка испытательного периода и инициирует отправку промежуточных проверок
     * каждый пять дней или окончательного отчета
     */
    @Override
    public void decreaseTrialPeriodDays() {
        LocalDate currentDate = LocalDate.now();
        List<AdoptionRecord> adoptionRecords = adoptionRecordRepository.findByTrialPeriodEndAfterAndState(
                currentDate, AdoptionRecord.TrialPeriodState.PROBATION);
        List<AdoptionRecord> updatedAdoptionRecords = adoptionRecords.stream()
                .filter(adoptionRecord -> adoptionRecord.getTrialPeriodDays() > 0)
                .peek(adoptionRecord -> {
                    int trialPeriodDays = adoptionRecord.getTrialPeriodDays() - 1;
                    adoptionRecord.setTrialPeriodDays(trialPeriodDays);
                    checkEvents(adoptionRecord);
                })
                .collect(Collectors.toList());
        adoptionRecordRepository.saveAll(updatedAdoptionRecords);
    }


    private void checkEvents(AdoptionRecord adoptionRecord) {
        List<Report> reports = adoptionRecordRepository.findAllReportsByAdoptionRecord(adoptionRecord);
        Long userChatId = adoptionRecord.getUser().getChatId();
        int trialPeriodDays = adoptionRecord.getTrialPeriodDays();
        int overallScore = reportAnalyser.analyzeReportsResults(adoptionRecord, reports, userChatId);
        List<User> volunteers = userService.findAllByState(VOLUNTEER);
        String notificationAction = ReportAnalyser.getNotificationAction(overallScore);
        if (trialPeriodDays == 0) {//сдан последний отчет
            for (User volunteer : volunteers) {//отправляем результаты волонтерам
                notificationSender.sendNotificationToVolunteerAboutFinalCheck(notificationAction, volunteer.getChatId(), userChatId);
            }
            notificationSender.sendNotificationToAdopterAboutFinalCheck(notificationAction, userChatId);//результаты пользователю
            return;
        }
        if (trialPeriodDays % 5 == 0) {//промежуточная проверка каждые пять дней
            for (User volunteer : volunteers) {//отправляем результаты волонтерам
                notificationSender.sendNotificationToVolunteerAboutCheck(notificationAction, volunteer.getChatId(), userChatId);
            }
            notificationSender.sendNotificationToAdopterAboutCheck(notificationAction, userChatId);//результаты пользователю
            adoptionRecordRepository.save(adoptionRecord);
        }
    }
}
