package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.TrialPeriodState;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.repository.AdoptionRecordRepository;
import pro.sky.telegramBot.repository.UserRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.service.VolunteerService;

import java.time.LocalDate;
import java.util.List;

import static pro.sky.telegramBot.enums.PetType.NOPET;
import static pro.sky.telegramBot.enums.UserState.PROBATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionRecordServiceImpl implements AdoptionRecordService {
    private final UserRepository userRepository;
    private final AdoptionRecordRepository adoptionRecordRepository;
    private final UserService userService;
    private final MessageSender messageSender;
    private final VolunteerService volunteerService;

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
                List<Volunteer> volunteers = volunteerService.findAll();
                for(Volunteer volunteer : volunteers) {
                    messageSender.sendMissingPetMessageToVolunteer(user, volunteer.getChatId());
                }
            }
            adoptionRecordRepository.save(adoptionRecord);
        }
    }
    @Override
    public void addNewReportToAdoptionRecord(Report newReport, int reportResult, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        if(user != null && user.getAdoptionRecord() != null){
            AdoptionRecord adoptionRecord = user.getAdoptionRecord();
            adoptionRecord.setRatingTotal(adoptionRecord.getRatingTotal() + reportResult);
        }
    }

    @Override
    public void checkNewAdopter() {
        List<User> newAdopters = userService.findAllByAdoptionRecordIsNullAndState(PROBATION);
        if(!newAdopters.isEmpty()){
            for(User user : newAdopters){
                user.setAdoptionRecord(new AdoptionRecord());
                userService.update(user);
                messageSender.sendNotificationToAdopterAboutDailyReportPhotoMessage(user.getChatId());
            }
        }
    }

    @Override
    public void informAdopterAboutStartReporting() {
        List<User> adopters = userService.findAllByState(PROBATION);
        if(!adopters.isEmpty()){
            for(User user : adopters){
                messageSender.sendNotificationToAdopterAboutStartReportPhotoMessage(user.getChatId());
            }
        }

    }

    @Override
    public void informAdopterAboutEndReporting() {
        List<User> adopters = userService.findAllByState(PROBATION);
        if(!adopters.isEmpty()){
            for(User user : adopters){
                messageSender.sendNotificationToAdopterAboutEndReportPhotoMessage(user.getChatId());
            }
        }
    }

}
