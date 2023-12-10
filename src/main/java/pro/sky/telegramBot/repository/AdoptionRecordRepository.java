package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.Adoption.Report;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.Adoption.Report;
import pro.sky.telegramBot.model.users.User;

import java.util.List;

public interface AdoptionRecordRepository extends JpaRepository<AdoptionRecord, Long> {
        List<Report> findReportsByUser(User user);

        @Query("SELECT DISTINCT ar.user FROM adoption_record ar " +
               "LEFT JOIN ar.reports r " +
               "WHERE ar.state = pro.sky.telegramBot.enums.TrialPeriodState.PROBATION " +
               "AND (r.reportDateTime IS NULL OR r.reportDateTime < CURRENT_DATE)")
        List<User> findUsersWithProbationAndNoReportToday();

        @Query("SELECT DISTINCT ar.user FROM adoption_record ar " +
               "JOIN ar.reports r " +
               "WHERE r.reportDateTime = CURRENT_DATE " +
               "AND (r.data IS NULL OR r.data = '')")
        List<User> findUsersWithReportTodayAndNoPhoto();
}
