package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.TrialPeriodState;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.AdoptionRecordRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegramBot.enums.PetType.NOPET;
import static pro.sky.telegramBot.enums.UserState.PROBATION;
import static pro.sky.telegramBot.enums.UserState.VOLUNTEER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionRecordServiceImpl implements AdoptionRecordService {
    private final AdoptionRecordRepository adoptionRecordRepository;
    private final UserService userService;
    private final MessageSender messageSender;

    //Метод для получения текущего отчета
    @Override
    public Report getCurrentReport(Long id, LocalDate date) {
        User user = userService.findById(id).orElse(null);
        if (user != null) {
            List<Report> reports = adoptionRecordRepository.findReportsByUser(user);
            for (Report report : reports) {
                if (report.getReportDateTime().equals(date)) {
                    return report;
                }
            }
        }
        return new Report();
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
            adoptionRecord.setState(TrialPeriodState.PROBATION);
            adoptionRecord.setRatingTotal(0);
            if (pet != null) {
                adoptionRecord.setPet(pet);
            } else {
                pet = new Pet();
                pet.setType(NOPET);
                adoptionRecord.setPet(pet);
                List<User> volunteers = userService.findAllByState(VOLUNTEER);
                for (User volunteer : volunteers) {
                    messageSender.sendMissingPetMessageToVolunteerPhotoMessage(user.getChatId(), volunteer.getChatId());
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
                messageSender.sendNotificationToAdopterAboutDailyReportPhotoMessage(user.getChatId());
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
                messageSender.sendNotificationToAdopterAboutStartReportPhotoMessage(user.getChatId());
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
                messageSender.sendNotificationToAdopterAboutEndReportPhotoMessage(user.getChatId());
            }
        }
    }

}
