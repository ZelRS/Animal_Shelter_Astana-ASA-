package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.AdoptionRecordRepository;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionRecordServiceImpl implements AdoptionRecordService {
    private final AdoptionRecordRepository adoptionRecordRepository;
    private final UserService userService;

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
}
