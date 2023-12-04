package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.users.User;

import java.util.List;

public interface AdoptionRecordRepository extends JpaRepository<AdoptionRecord, Long> {
        List<Report> findReportsByUser(User user);
}
